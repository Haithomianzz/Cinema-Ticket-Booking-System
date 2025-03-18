package com.client;

import com.Backend.Dao.*;
import com.Backend.Entities.*;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import java.sql.Connection;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
public class Client implements Runnable {
    private static Connection connection;
    public static void setConnection(Connection connection) {
        Client.connection = connection;
    }
    public static Connection getConnection() {
        return connection;
    }

    private static HashMap<Integer, Customer> customerMap = new HashMap<>();
    private static HashMap<Integer, Cinema> cinemaMap = new HashMap<>();
    private static HashMap<Integer, Booking> bookingMap = new HashMap<>();
    private static HashMap<Integer, Movie> movieMap = new HashMap<>();
    private static HashMap<Integer, Showtime> showtimeMap = new HashMap<>();

    private static HashMap<Pair<Integer, Integer>, Hall> hallMap = new HashMap<>();

    private static HashMap<Integer, Seat> seatMap = new HashMap<>();
    private static HashMap<Integer, Ticket> ticketMap = new HashMap<>();
    private static HashMap<Integer, Payment> paymentMap = new HashMap<>();

    public static HashMap<Integer, Customer> getCustomerMap() { return customerMap; }
    public static HashMap<Integer, Cinema> getCinemaMap() { return cinemaMap; }
    public static HashMap<Integer, Booking> getBookingMap() { return bookingMap; }
    public static HashMap<Integer, Movie> getMovieMap() { return movieMap; }
    public static HashMap<Pair<Integer, Integer>, Hall> getHallMap() { return hallMap; }
    public static HashMap<Integer, Showtime> getShowtimeMap() { return showtimeMap; }
    public static HashMap<Integer, Seat> getSeatMap() { return seatMap;}
    public static HashMap<Integer, Ticket> getTicketMap() { return ticketMap; }
    public static HashMap<Integer, Payment> getPaymentMap() { return paymentMap; }

    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        try {
            Future<HashMap<Integer, Customer>> customerFuture = executor.submit(() -> CustomerDAO.getAllCustomers(connection));
            Future<HashMap<Integer, Movie>> movieFuture = executor.submit(() -> MovieDAO.getAllMovies(connection));
            Future<HashMap<Integer, Cinema>> cinemaFuture = executor.submit(() -> CinemaDAO.getAllCinemas(connection));
            Future<HashMap<Pair<Integer,Integer>, Hall>> hallFuture = executor.submit(() -> HallDAO.getAllHalls(connection, cinemaMap));
            Future<HashMap<Integer, Booking>> bookingFuture = executor.submit(() -> BookingDAO.getAllBookings(connection, customerMap));
            Future<HashMap<Integer, Seat>> seatFuture = executor.submit(() -> SeatDAO.getAllSeats(connection, hallMap));
            Future<HashMap<Integer, Showtime>> showtimeFuture = executor.submit(() -> ShowtimeDAO.getAllShowtimes(connection, movieMap, hallMap));
            Future<HashMap<Integer, Ticket>> ticketFuture = executor.submit(() -> TicketDAO.getAllTickets(connection, bookingMap, showtimeMap, seatMap));
            Future<HashMap<Integer, Payment>> paymentFuture = executor.submit(() -> PaymentDAO.getAllPayments(connection, bookingMap));
            Future<Integer> maxCustomerIdFuture = executor.submit(() -> CustomerDAO.getMaxCustomerId(connection));
            Future<Integer> maxCinemaIdFuture = executor.submit(() -> CinemaDAO.getMaxCinemaId(connection));
            Future<Integer> maxMovieIdFuture = executor.submit(() -> MovieDAO.getMaxMovieId(connection));
            customerMap = customerFuture.get();
            cinemaMap = cinemaFuture.get();
            movieMap = movieFuture.get();
            hallMap = hallFuture.get();
            bookingMap = bookingFuture.get();
            seatMap = seatFuture.get();
            showtimeMap = showtimeFuture.get();
            ticketMap = ticketFuture.get();
            paymentMap = paymentFuture.get();
            Customer.setCustomerIdCounter(maxCustomerIdFuture.get());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
    public static void addCustomer(Customer customer) {
        customerMap.put(customer.getCustomerId(), customer);
    }
    public static void addCinema(Cinema cinema) {
        cinemaMap.put(cinema.getCinemaId(), cinema);
    }
    public static void addMovie(Movie movie) {
        movieMap.put(movie.getMovieId(), movie);
    }
    public static void addHall(Hall hall) {
        hallMap.put(new Pair<>(hall.getCinemaId(), hall.getHallNumber()), hall);

    }
}