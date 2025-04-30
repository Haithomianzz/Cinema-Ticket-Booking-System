package com.Backend.Dao;

import com.Backend.Entities.Booking;
import com.Backend.Entities.Seat;
import com.Backend.Entities.Showtime;
import com.Backend.Entities.Ticket;
import org.javatuples.Triplet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class TicketDAO {
    //    private static final String GET_TICKET_BY_ID = "SELECT * FROM ticket WHERE ticket_id = ?";
    private static final String GET_ALL_TICKETS = "SELECT * FROM ticket";
    private static final String INSERT_TICKET = "INSERT INTO ticket (booking_id, showtime_id, seat_id, qr_code) VALUES (?, ?, ?, ?)";
    private static final String DELETE_TICKET = "DELETE FROM ticket WHERE booking_id = ? AND showtime_id = ? AND seat_id = ?";
    private static final String DELETE_TICKET_BY_BOOKING_ID = "DELETE FROM ticket WHERE booking_id = ?";
    private static final String DELETE_TICKET_BY_SHOWTIME_ID = "DELETE FROM ticket WHERE showtime_id = ?";
    private static final String DELETE_TICKET_BY_SEAT_ID = "DELETE FROM ticket WHERE seat_id = ?";

    public static HashMap<Triplet<Integer,Integer,Integer>, Ticket> getAllTickets(Connection connection, HashMap<Integer, Booking> bookingMap,
                                                         HashMap<Integer, Showtime> showtimeMap, HashMap<Integer, Seat> seatMap) {
        HashMap<Triplet<Integer,Integer,Integer>, Ticket> ticketMap = new HashMap<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_TICKETS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Booking booking = bookingMap.get(resultSet.getInt(1));
                Showtime showtime = showtimeMap.get(resultSet.getInt(2));
                Seat seat = seatMap.get(resultSet.getInt(3));
                Ticket ticket = new Ticket(booking, showtime, seat);
                ticketMap.put(new Triplet<>(booking.getBookingId(),showtime.getShowtimeId(),seat.getSeatId()), ticket);
                booking.addTicket(ticket);
                showtime.addTicket(ticket);
                seat.addTicket(ticket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticketMap;
    }
    public static boolean insertTicket(Connection connection, Ticket ticket){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TICKET);
            preparedStatement.setInt(1, ticket.getBooking().getBookingId());
            preparedStatement.setInt(2, ticket.getShowtime().getShowtimeId());
            preparedStatement.setInt(3, ticket.getSeat().getSeatId());
            preparedStatement.setString(4, ticket.getQrCode());
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Error: Unable to insert ticket.");
        return false;
    }
    public static boolean deleteTicket(Connection connection, Ticket Ticket){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TICKET);
            preparedStatement.setInt(1, Ticket.getBooking().getBookingId());
            preparedStatement.setInt(2, Ticket.getShowtime().getShowtimeId());
            preparedStatement.setInt(3, Ticket.getSeat().getSeatId());
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Error: Unable to delete ticket.");
        return false;
    }
    public static boolean deleteTicketByShowtime(Connection connection, Showtime showtime){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TICKET_BY_SHOWTIME_ID);
            preparedStatement.setInt(1, showtime.getShowtimeId());
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Error: Unable to delete ticket.");
        return false;
    }
    public static boolean deleteTicketByBooking(Connection connection, Booking booking){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TICKET_BY_BOOKING_ID);
            preparedStatement.setInt(1, booking.getBookingId());
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Error: Unable to delete ticket.");
        return false;
    }
    public static boolean deleteTicketBySeat(Connection connection, Seat seat){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TICKET_BY_SEAT_ID);
            preparedStatement.setInt(1, seat.getSeatId());
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Error: Unable to delete ticket.");
        return false;
    }




}