package com.Backend.Dao;

import com.Backend.Entities.Booking;
import com.Backend.Entities.Customer;
import com.Frontend.Date;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class BookingDAO {
    private static final String GET_MAX_BOOKING_ID = "SELECT MAX(booking_id) FROM booking";
    private static final String GET_ALL_BOOKINGS = "SELECT booking_id, customer_id, total_price, booking_date, booking_status FROM booking";
    private static final String CONFIRM_BOOKING = "UPDATE booking SET booking_status = 'CONFIRMED' WHERE booking_id = ?";
    private static final String INSERT_BOOKING = "INSERT INTO booking (customer_id, total_price, booking_date, booking_status) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_BOOKING = "UPDATE booking SET customer_id = ?, total_price = ?, booking_date = ?, booking_status = ? WHERE booking_id = ?";
    private static final String DELETE_BOOKING = "DELETE FROM booking WHERE booking_id = ?";
    private static final String CANCEL_BOOKING = "EXEC CancelBooking @booking_id = ?";
    public static int getMaxBookingId(Connection connection){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_MAX_BOOKING_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Failed to get max booking id");
        return 0;
    }
    public static HashMap<Integer,Booking> getAllBookings(Connection connection, HashMap<Integer, Customer> customerMap) {
        HashMap<Integer, Booking> bookingMap = new HashMap<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BOOKINGS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Customer customer = customerMap.get(resultSet.getInt(2));
                Booking booking = new Booking(
                        resultSet.getInt(1),
                        customer,
                        resultSet.getDouble(3),
                        new Date(resultSet.getString(4)),
                        resultSet.getString(5)
                );
                bookingMap.put(booking.getBookingId(), booking);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return bookingMap;
    }
    public static boolean insertBooking(Connection connection, Booking booking) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BOOKING);
            preparedStatement.setInt(1, booking.getCustomer().getCustomerId());
            preparedStatement.setDouble(2, booking.getTotalPrice());
            preparedStatement.setString(3, booking.getBookingDate().toString());
            preparedStatement.setString(4, booking.getBookingStatus().toString().toUpperCase());
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Failed to insert booking");
        return false;
    }
    public static boolean updateBooking(Connection connection, Booking booking){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BOOKING);
            preparedStatement.setInt(1, booking.getCustomer().getCustomerId());
            preparedStatement.setDouble(2, booking.getTotalPrice());
            preparedStatement.setString(3, booking.getBookingDate().toString());
            preparedStatement.setString(4, booking.getBookingStatus().toString().toUpperCase());
            preparedStatement.setInt(5, booking.getBookingId());
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Failed to update booking");
        return false;
    }
    public static boolean deleteBooking(Connection connection, Booking booking){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(CANCEL_BOOKING);
            preparedStatement.setInt(1, booking.getBookingId());
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(DELETE_BOOKING);
            preparedStatement.setInt(1, booking.getBookingId());
            preparedStatement.executeUpdate();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        System.err.println("Failed to delete booking");
        return false;
    }
    public static boolean confirmBooking(Connection connection, Booking booking){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(CONFIRM_BOOKING);
            preparedStatement.setInt(1, booking.getBookingId());
            return preparedStatement.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        System.err.println("Failed to confirm booking");
        return false;
    }
    public static boolean cancelBooking(Connection connection, Booking booking){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(CANCEL_BOOKING);
            preparedStatement.setInt(1, booking.getBookingId());
            return preparedStatement.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        System.err.println("Failed to cancel booking");
        return false;
    }
}