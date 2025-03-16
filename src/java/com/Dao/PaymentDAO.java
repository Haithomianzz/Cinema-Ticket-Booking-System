package com.Dao;

import com.Entities.Payment;

import java.sql.Connection;
import java.util.HashMap;

public class PaymentDAO {
    private static final String GET_ALL_PAYMENTS = "SELECT * FROM payment";
    private static final String GET_PAYMENT_BY_ID = "SELECT * FROM payment WHERE payment_id = ?";
    private static final String INSERT_PAYMENT = "INSERT INTO payment (booking_id, payment_method, transaction_id, amount, payment_date, status) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_PAYMENT = "UPDATE payment SET booking_id = ?, payment_method = ?, transaction_id = ?, amount = ?, payment_date = ?, status = ? WHERE payment_id = ?";
    private static final String DELETE_PAYMENT = "DELETE FROM payment WHERE payment_id = ?";

    public static HashMap<Integer, Payment> getAllPayments(Connection connection, HashMap<Integer, Booking> bookingMap) {
        // Implementation here
    }
}