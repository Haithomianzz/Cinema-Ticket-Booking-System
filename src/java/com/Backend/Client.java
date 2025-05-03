package com.Backend;
import com.Frontend.Main;
import org.javatuples.Triplet;
import com.Backend.Dao.*;
import com.Backend.Entities.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
public class Client implements Runnable {
    private static Connection connection;

    private static HashMap<Integer, Customer> customerMap = new HashMap<>();
    private static HashMap<Integer, Booking> bookingMap = new HashMap<>();
    private static HashMap<Integer, Movie> movieMap = new HashMap<>();
    private static HashMap<Integer, Showtime> showtimeMap = new HashMap<>();
    private static HashMap<Integer, Hall> hallMap = new HashMap<>();
    private static HashMap<Integer, Seat> seatMap = new HashMap<>();
    private static HashMap<Triplet<Integer,Integer,Integer>, Ticket> ticketMap = new HashMap<>();

    public static Connection getConnection() { return connection; }
    public static HashMap<Integer, Customer> getCustomerMap() { return customerMap; }
    public static HashMap<Integer, Booking> getBookingMap() { return bookingMap; }
    public static HashMap<Integer, Movie> getMovieMap() { return movieMap; }
    public static HashMap<Integer, Hall> getHallMap() { return hallMap; }
    public static HashMap<Integer, Showtime> getShowtimeMap() { return showtimeMap; }
    public static HashMap<Integer, Seat> getSeatMap() { return seatMap;}
    public static HashMap<Triplet<Integer,Integer,Integer>, Ticket> getTicketMap() { return ticketMap; }

    static {
        try {
            connection = DatabaseConnector.connect();
        } catch (SQLException e) {
            System.out.println("Error: Unable to establish database connection.");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        try {
            // Load Customers, Movies, and Halls first
            Future<HashMap<Integer, Customer>> customerFuture = executor.submit(() -> CustomerDAO.getAllCustomers(connection));
            Future<HashMap<Integer, Movie>> movieFuture = executor.submit(() -> MovieDAO.getAllMovies(connection));
            Future<HashMap<Integer, Hall>> hallFuture = executor.submit(() -> HallDAO.getAllHalls(connection));

            // Wait for the first set of tasks to complete
            customerMap = customerFuture.get();
            movieMap = movieFuture.get();
            hallMap = hallFuture.get();

            // Load Bookings, Showtimes, and Seats next
            Future<HashMap<Integer, Booking>> bookingFuture = executor.submit(() -> BookingDAO.getAllBookings(connection, customerMap));
            Future<HashMap<Integer, Seat>> seatFuture = executor.submit(() -> SeatDAO.getAllSeats(connection, hallMap));

            // Wait for the second set of tasks to complete
            bookingMap = bookingFuture.get();
            seatMap = seatFuture.get();

            Future<HashMap<Integer, Showtime>> showtimeFuture = executor.submit(() -> ShowtimeDAO.getAllShowtimes(connection, movieMap, hallMap));
            showtimeMap = showtimeFuture.get();
            // Allocate seats and load Tickets last
            Future<Boolean> allocateSeatsFuture = executor.submit(() -> ShowtimeDAO.allocateSeats(connection, showtimeMap, seatMap));
            Future<HashMap<Triplet<Integer, Integer, Integer>, Ticket>> ticketFuture = executor.submit(() -> TicketDAO.getAllTickets(connection, bookingMap, showtimeMap, seatMap));

            if (!allocateSeatsFuture.get())
                System.out.println("Error: Unable to allocate seats.");
            ticketMap = ticketFuture.get();

            // Get max IDs to set static counters for automatic ID generation
            Future<Integer> maxCustomerIdFuture = executor.submit(() -> CustomerDAO.getMaxCustomerId(connection));
            Future<Integer> maxMovieIdFuture = executor.submit(() -> MovieDAO.getMaxMovieId(connection));
            Future<Integer> maxHallIdFuture = executor.submit(() -> HallDAO.getMaxHallId(connection));
            Future<Integer> maxBookingIdFuture = executor.submit(() -> BookingDAO.getMaxBookingId(connection));
            Future<Integer> maxSeatIdFuture = executor.submit(() -> SeatDAO.getMaxSeatId(connection));
            Future<Integer> maxShowtimeIdFuture = executor.submit(() -> ShowtimeDAO.getMaxShowtimeId(connection));

            Customer.setCustomerIdCounter(maxCustomerIdFuture.get());
            Movie.setMovieIdCounter(maxMovieIdFuture.get());
            Hall.setHallIdCounter(maxHallIdFuture.get());
            Booking.setBookingIdCounter(maxBookingIdFuture.get());
            Seat.setSeatIdCounter(maxSeatIdFuture.get());
            Showtime.setShowtimeIdCounter(maxShowtimeIdFuture.get());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    public static boolean addCustomer(Customer customer) {
        if (CustomerDAO.insertCustomer(connection, customer)) {
            customerMap.put(customer.getCustomerId(), customer);
            return true;
        }
        return true;
    }
    public static boolean addMovie(Movie movie) {
        if (!MovieDAO.insertMovie(connection, movie)) return false;
        movieMap.put(movie.getMovieId(), movie);
        return true;
    }
    public static boolean addHall(Hall hall, int numberOfSeats) {
        if (!HallDAO.insertHall(connection, hall, numberOfSeats)) return false;
        hallMap.put(hall.getHallNumber(), hall);
        int count = 0;
        while (count < numberOfSeats) {
            for (int i = 1; i <= 3; i++) {
                for (int j = 1; j <=6; j++) {
                    if (count >= numberOfSeats) break;
                    count++;
                    Seat seat = new Seat(hall, i, j);
                    seatMap.put(seat.getSeatId(), seat);
                }
            }
        }
        return true;
    }
    public static boolean addBooking(Booking booking) {
        if (!BookingDAO.insertBooking(connection, booking)) return false;
        bookingMap.put(booking.getBookingId(), booking);
        return true;
    }
    public static boolean addSeat(Seat seat) {
        if (!SeatDAO.insertSeat(connection, seat)) return false;
        seatMap.put(seat.getSeatId(), seat);
        return true;
    }
    public static boolean addShowtime(Showtime showtime) {
        if (!ShowtimeDAO.insertShowtime(connection, showtime)) return false;
        showtimeMap.put(showtime.getShowtimeId(), showtime);
        return true;
    }
    public static boolean addTicket(Ticket ticket) {
        if (!TicketDAO.insertTicket(connection, ticket)) return false;
        ticketMap.put(new Triplet<>(ticket.getBooking().getBookingId(),ticket.getShowtime().getShowtimeId(),ticket.getSeat().getSeatId()), ticket);
        return true;
    }
    public static boolean removeCustomer(Customer customer) {
        if (!CustomerDAO.deleteCustomer(connection, customer)) return false;
        customerMap.remove(customer.getCustomerId());
        if (!customer.getBookings().isEmpty()) {
            ArrayList<Booking> bookings = new ArrayList<>(customer.getBookings()) ;
            for (Booking booking : bookings) {
                bookingMap.remove(booking.getBookingId());
                booking.cancelBooking();
                if (!booking.getTickets().isEmpty()) {
                    ArrayList<Ticket> tickets = new ArrayList<>(booking.getTickets()) ;
                    for (Ticket ticket : tickets) {
                        ticketMap.remove(new Triplet<>(ticket.getBooking().getBookingId(),ticket.getShowtime().getShowtimeId(),ticket.getSeat().getSeatId()));
                        ticket.cancelTicket();
                    }
                }
            }
        }
        return true;
    }
    public static boolean removeMovie(Movie movie) {
        if (!MovieDAO.deleteMovie(connection, movie)) return false;
        movieMap.remove(movie.getMovieId());
        if (!movie.getShowtimes().isEmpty()) {
            ArrayList<Showtime> showtimes = new ArrayList<>(movie.getShowtimes()) ;
            for (Showtime showtime : showtimes) {
                showtimeMap.remove(showtime.getShowtimeId());
                showtime.cancelShowtime();
                if (!showtime.getTickets().isEmpty()) {
                    ArrayList<Ticket> tickets = new ArrayList<>(showtime.getTickets()) ;
                    for (Ticket ticket : tickets) {
                        ticketMap.remove(new Triplet<>(ticket.getBooking().getBookingId(),ticket.getShowtime().getShowtimeId(),ticket.getSeat().getSeatId()));
                        ticket.cancelTicket();
                    }
                }
            }
        }
        return true;
    }
    public static boolean removeHall(Hall hall) {
        if (!HallDAO.deleteHall(connection, hall)) return false;
        hallMap.remove(hall.getHallNumber());
        if (!hall.getSeats().isEmpty()) {
            ArrayList<Seat> seats = new ArrayList<>(hall.getSeats()) ;
            for (Seat seat : seats) {
                seatMap.remove(seat.getSeatId());
                seat.removeSeat();
            }
        }
        if (!hall.getShowtimes().isEmpty()) {
            ArrayList<Showtime> showtimes = new ArrayList<>(hall.getShowtimes()) ;
            for (Showtime showtime : showtimes) {
                showtimeMap.remove(showtime.getShowtimeId());
                showtime.cancelShowtime();
                if (!showtime.getTickets().isEmpty()) {
                    ArrayList<Ticket> tickets = new ArrayList<>(showtime.getTickets()) ;
                    for (Ticket ticket : tickets) {
                        ticketMap.remove(new Triplet<>(ticket.getBooking().getBookingId(),ticket.getShowtime().getShowtimeId(),ticket.getSeat().getSeatId()));
                        ticket.cancelTicket();
                    }
                }
            }
        }

        return true;
    }
    public static boolean removeBooking(Booking booking) {
        if (!BookingDAO.deleteBooking(connection, booking)) return false;
        bookingMap.remove(booking.getBookingId());
        booking.deleteBooking();
        if (!booking.getTickets().isEmpty()) {
            ArrayList<Ticket> tickets = new ArrayList<>(booking.getTickets()) ;
            for (Ticket ticket : tickets) {
                ticketMap.remove(new Triplet<>(ticket.getBooking().getBookingId(),ticket.getShowtime().getShowtimeId(),ticket.getSeat().getSeatId()));
                ticket.cancelTicket();
            }
        }
        return true;
    }
    public static boolean removeSeat(Seat seat) {
        if (!SeatDAO.deleteSeat(connection, seat)) return false;
        seatMap.remove(seat.getSeatId());
        seat.removeSeat();
        if (!seat.getTickets().isEmpty()) {
            ArrayList<Ticket> tickets = new ArrayList<>(seat.getTickets()) ;
            for (Ticket ticket : tickets) {
                ticketMap.remove(new Triplet<>(ticket.getBooking().getBookingId(),ticket.getShowtime().getShowtimeId(),ticket.getSeat().getSeatId()));
                ticket.cancelTicket();
            }
        }
        return true;
    }
    public static boolean removeShowtime(Showtime showtime) {
        if (!ShowtimeDAO.deleteShowtime(connection, showtime)) return false;
        showtimeMap.remove(showtime.getShowtimeId());
        showtime.cancelShowtime();
        if (!showtime.getTickets().isEmpty()) {
            ArrayList<Ticket> tickets = new ArrayList<>(showtime.getTickets()) ;
            for (Ticket ticket : tickets) {
                ticketMap.remove(new Triplet<>(ticket.getBooking().getBookingId(),ticket.getShowtime().getShowtimeId(),ticket.getSeat().getSeatId()));
                ticket.cancelTicket();
            }
        }
        return true;
    }
    public static boolean removeTicket(Ticket ticket) {
        if (!TicketDAO.deleteTicket(connection, ticket)) return false;
        ticket.cancelTicket();
        ticketMap.remove(new Triplet<>(ticket.getBooking().getBookingId(),ticket.getShowtime().getShowtimeId(),ticket.getSeat().getSeatId()));
        return true;
    }
    public static void closeConnection() {
        if (connection == null) {
            System.out.println("Connection is already closed.");
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean verifyCredentials(String email, String password) {
        Integer userid = AuthenticationDAO.authenticateUser(connection, email, password);
        if (userid > 0) {
            Main.setCurrentUser(customerMap.get(userid));
            Main.setCurrentUserType(Main.UserType.CUSTOMER);
            System.out.println("User authenticated successfully.");
            return true;
        } else if (userid == 0) {
            Main.setCurrentUserType(Main.UserType.ADMIN);
            Main.setCurrentUser(customerMap.get(0));
            System.out.println("Admin authenticated successfully.");
            return true;
        } else {
            System.out.println("Error: Invalid email or password.");
            return false;
        }
    }
    public static boolean changePasswordAndEmail(String password, String email) {
        if (Main.getCurrentUserType() == Main.UserType.CUSTOMER && AuthenticationDAO.changePasswordAndEmail(connection, password, email, Main.getCurrentUser())) {
            Main.getCurrentUser().setPassword(password);
            Main.getCurrentUser().setEmail(email);
            System.out.println("Password and email changed successfully.");
            return true;
        } else {
            System.out.println("Error: Only customers can change their password and email.");
            return false;
        }
    }
    public static Customer getCustomerByEmail(String email) {
        Integer customerId = AuthenticationDAO.getCustomerIdByEmail(connection, email);
        if (customerId != null && customerMap.containsKey(customerId)) {
            return customerMap.get(customerId);
        }
        return null;
    }
    public static Boolean confirmBooking(Booking booking) {
        if (Main.getCurrentUserType() == Main.UserType.CUSTOMER && BookingDAO.confirmBooking(connection, booking)) {
            booking.setBookingStatus(Booking.BookingStatus.CONFIRMED);
            return true;
        }
        return false;
    }
    public static Boolean cancelBooking(Booking booking) {
        if (Main.getCurrentUserType() == Main.UserType.CUSTOMER && BookingDAO.cancelBooking(connection, booking)) {
            ArrayList<Ticket> tickets = new ArrayList<>(booking.getTickets()) ;
            for (Ticket ticket : tickets) {
                ticketMap.remove(new Triplet<>(ticket.getBooking().getBookingId(),ticket.getShowtime().getShowtimeId(),ticket.getSeat().getSeatId()));
                ticket.cancelTicket();
            }
            booking.setBookingStatus(Booking.BookingStatus.CANCELLED);
            return true;
        }
        return false;
    }


    public static boolean updateCustomer(Customer customer) { return CustomerDAO.updateCustomer(connection, customer); }
    public static boolean updateMovie(Movie movie) { return MovieDAO.updateMovie(connection, movie); }
//    public static boolean updateHall(Hall hall) { return HallDAO.updateHall(connection, hall); }
//    public static boolean updateBooking(Booking booking) { return BookingDAO.updateBooking(connection, booking); }
//    public static boolean updateSeat(Seat seat) { return SeatDAO.updateSeat(connection, seat); }
    public static boolean updateShowtime(Showtime showtime) { return ShowtimeDAO.updateShowtime(connection, showtime); }

}