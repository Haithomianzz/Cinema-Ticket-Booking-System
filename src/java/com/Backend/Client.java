package com.Backend;

import com.Backend.Dao.*;
import com.Backend.Entities.*;

import java.sql.Connection;
import java.sql.SQLException;
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
    private static HashMap<Integer, Ticket> ticketMap = new HashMap<>();

    public static Connection getConnection() { return connection; }
    public static HashMap<Integer, Customer> getCustomerMap() { return customerMap; }
    public static HashMap<Integer, Booking> getBookingMap() { return bookingMap; }
    public static HashMap<Integer, Movie> getMovieMap() { return movieMap; }
    public static HashMap<Integer, Hall> getHallMap() { return hallMap; }
    public static HashMap<Integer, Showtime> getShowtimeMap() { return showtimeMap; }
    public static HashMap<Integer, Seat> getSeatMap() { return seatMap;}
    public static HashMap<Integer, Ticket> getTicketMap() { return ticketMap; }

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
            // Load all data from the database using multithreading
            Future<HashMap<Integer, Customer>> customerFuture = executor.submit(() -> CustomerDAO.getAllCustomers(connection));
            Future<HashMap<Integer, Movie>> movieFuture = executor.submit(() -> MovieDAO.getAllMovies(connection));
            Future<HashMap<Integer, Hall>> hallFuture = executor.submit(() -> HallDAO.getAllHalls(connection));
            Future<HashMap<Integer, Booking>> bookingFuture = executor.submit(() -> BookingDAO.getAllBookings(connection, customerMap));
            Future<HashMap<Integer, Seat>> seatFuture = executor.submit(() -> SeatDAO.getAllSeats(connection, hallMap));
            Future<HashMap<Integer, Showtime>> showtimeFuture = executor.submit(() -> ShowtimeDAO.getAllShowtimes(connection, movieMap, hallMap));
            Future<Boolean> allocateSeatsFuture = executor.submit(() -> ShowtimeDAO.allocateSeats(connection, showtimeMap, seatMap));
            Future<HashMap<Integer, Ticket>> ticketFuture = executor.submit(() -> TicketDAO.getAllTickets(connection, bookingMap, showtimeMap, seatMap));
            // Get max IDs to set static counters for automatic ID generation when inserting new records
            Future<Integer> maxCustomerIdFuture = executor.submit(() -> CustomerDAO.getMaxCustomerId(connection));
            Future<Integer> maxMovieIdFuture = executor.submit(() -> MovieDAO.getMaxMovieId(connection));
            Future<Integer> maxHallIdFuture = executor.submit(() -> HallDAO.getMaxHallId(connection));
            Future<Integer> maxBookingIdFuture = executor.submit(() -> BookingDAO.getMaxBookingId(connection));
            Future<Integer> maxSeatIdFuture = executor.submit(() -> SeatDAO.getMaxSeatId(connection));
            Future<Integer> maxShowtimeIdFuture = executor.submit(() -> ShowtimeDAO.getMaxShowtimeId(connection));
            Future<Integer> maxTicketIdFuture = executor.submit(() -> TicketDAO.getMaxTicketId(connection));
            // Wait for all futures to complete and retrieve the results to avoid blocking the main thread
            customerMap = customerFuture.get();
            movieMap = movieFuture.get();
            hallMap = hallFuture.get();
            bookingMap = bookingFuture.get();
            seatMap = seatFuture.get();
            showtimeMap = showtimeFuture.get();
            if (!allocateSeatsFuture.get())
                System.out.println("Error: Unable to allocate seats.");
            ticketMap = ticketFuture.get();

            Customer.setCustomerIdCounter(maxCustomerIdFuture.get());
            Movie.setMovieIdCounter(maxMovieIdFuture.get());
            Hall.setHallIdCounter(maxHallIdFuture.get());
            Booking.setBookingIdCounter(maxBookingIdFuture.get());
            Seat.setSeatIdCounter(maxSeatIdFuture.get());
            Showtime.setShowtimeIdCounter(maxShowtimeIdFuture.get());
            Ticket.setTicketIdCounter(maxTicketIdFuture.get());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    public static boolean addCustomer(Customer customer) {
        if (!CustomerDAO.insertCustomer(connection, customer)) return false;
        customerMap.put(customer.getCustomerId(), customer);
        return true;
    }
    public static boolean addMovie(Movie movie) {
        if (!MovieDAO.insertMovie(connection, movie)) return false;
        movieMap.put(movie.getMovieId(), movie);
        return true;
    }
    public static boolean addHall(Hall hall) {
        if (!HallDAO.insertHall(connection, hall)) return false;
        hallMap.put(hall.getHallNumber(), hall);
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
        ticketMap.put(ticket.getTicketId(), ticket);
        return true;
    }
    public static boolean removeCustomer(Customer customer) {
        if (!CustomerDAO.deleteCustomer(connection, customer)) return false;
        customerMap.remove(customer.getCustomerId());
        if (!customer.getBookings().isEmpty() && BookingDAO.deleteBookingsByCustomer(connection, customer)) {
            for (Booking booking : customer.getBookings()) {
                bookingMap.remove(booking.getBookingId());
                booking.cancelBooking();
                if (!booking.getTickets().isEmpty() && TicketDAO.deleteTicketByBooking(connection, booking)) {
                    for (Ticket ticket : booking.getTickets()) {
                        ticketMap.remove(ticket.getTicketId());
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
        if (!movie.getShowtimes().isEmpty() && ShowtimeDAO.deleteShowtimesByMovie(connection, movie)) {
            for (Showtime showtime : movie.getShowtimes()) {
                showtimeMap.remove(showtime.getShowtimeId());
                showtime.cancelShowtime();
                if (!showtime.getTickets().isEmpty() && TicketDAO.deleteTicketByShowtime(connection, showtime)) {
                    for (Ticket ticket : showtime.getTickets()) {
                        ticketMap.remove(ticket.getTicketId());
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
        if (!hall.getShowtimes().isEmpty() && ShowtimeDAO.deleteShowtimesByHall(connection, hall)) {
            for (Showtime showtime : hall.getShowtimes()) {
                showtimeMap.remove(showtime.getShowtimeId());
                showtime.cancelShowtime();
                if (!showtime.getTickets().isEmpty() && TicketDAO.deleteTicketByShowtime(connection, showtime)) {
                    for (Ticket ticket : showtime.getTickets()) {
                        ticketMap.remove(ticket.getTicketId());
                        ticket.cancelTicket();
                    }
                }
            }
        }
        if (!hall.getSeats().isEmpty() && SeatDAO.deleteSeatByHall(connection, hall)) {
            for (Seat seat : hall.getSeats()) {
                seatMap.remove(seat.getSeatId());
                seat.removeSeat();
            }
        }
        return true;
    }
    public static boolean removeBooking(Booking booking) {
        if (!BookingDAO.deleteBooking(connection, booking)) return false;
        bookingMap.remove(booking.getBookingId());
        booking.cancelBooking();
        if (!booking.getTickets().isEmpty() && TicketDAO.deleteTicketByBooking(connection, booking)) {
            for (Ticket ticket : booking.getTickets()) {
                ticketMap.remove(ticket.getTicketId());
                ticket.cancelTicket();
            }
        }
        return true;
    }
    public static boolean removeSeat(Seat seat) {
        if (!SeatDAO.deleteSeat(connection, seat)) return false;
        seatMap.remove(seat.getSeatId());
        seat.removeSeat();
        if (!seat.getTickets().isEmpty() && TicketDAO.deleteTicketBySeat(connection, seat)) {
            for (Ticket ticket : seat.getTickets()) {
                ticketMap.remove(ticket.getTicketId());
                ticket.cancelTicket();
            }
        }
        return true;
    }
    public static boolean removeShowtime(Showtime showtime) {
        if (!ShowtimeDAO.deleteShowtime(connection, showtime)) return false;
        showtimeMap.remove(showtime.getShowtimeId());
        showtime.cancelShowtime();
        if (!showtime.getTickets().isEmpty() && TicketDAO.deleteTicketByShowtime(connection, showtime)) {
            for (Ticket ticket : showtime.getTickets()) {
                ticketMap.remove(ticket.getTicketId());
                ticket.cancelTicket();
            }
        }
        return true;
    }
    public static boolean removeTicket(Ticket ticket) {
        if (!TicketDAO.deleteTicket(connection, ticket)) return false;
        ticketMap.remove(ticket.getTicketId());
        ticket.cancelTicket();
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
    public boolean updateCustomer(Customer customer) { return CustomerDAO.updateCustomer(connection, customer); }
    public boolean updateMovie(Movie movie) { return MovieDAO.updateMovie(connection, movie); }
    public boolean updateHall(Hall hall) { return HallDAO.updateHall(connection, hall); }
    public boolean updateBooking(Booking booking) { return BookingDAO.updateBooking(connection, booking); }
    public boolean updateSeat(Seat seat) { return SeatDAO.updateSeat(connection, seat); }
    public boolean updateShowtime(Showtime showtime) { return ShowtimeDAO.updateShowtime(connection, showtime); }
    public boolean updateTicket(Ticket ticket) { return TicketDAO.updateTicket(connection, ticket); }

}