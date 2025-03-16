package com.Dao;

import com.Entities.Booking;
import com.Entities.Customer;

import java.sql.Connection;
import java.util.HashMap;

public class BookingDAO {
    private static final String GET_ALL_BOOKINGS = "SELECT * FROM booking";
    private static final String GET_BOOKING_BY_ID = "SELECT * FROM booking WHERE booking_id = ?";
    private static final String INSERT_BOOKING = "INSERT INTO booking (customer_id, showtime_id, booking_date, total_price, booking_status) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_BOOKING = "UPDATE booking SET customer_id = ?, showtime_id = ?, booking_date = ?, total_price = ?, booking_status = ? WHERE booking_id = ?";
    private static final String DELETE_BOOKING = "DELETE FROM booking WHERE booking_id = ?";

    public static HashMap<Integer, Booking> getAllBookings(Connection connection, HashMap<Integer, Customer> customerMap) {
        // Implementation here
    }
}