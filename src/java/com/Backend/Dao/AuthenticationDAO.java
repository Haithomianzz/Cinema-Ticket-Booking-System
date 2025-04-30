package com.Backend.Dao;

import com.Backend.Entities.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthenticationDAO {
    private static final String AUTHENTICATE = "SELECT customer_id, password FROM customer WHERE email = ?";
    private static final String CHANGE_PASSWORD_AND_EMAIL = "UPDATE customer SET email = ?, password = ? WHERE customer_id = ?";
    private static final String GET_CUSTOMER_ID_BY_EMAIL = "SELECT customer_id FROM customer WHERE email = ?";

    public static Integer authenticateUser(Connection connection, String email, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(AUTHENTICATE);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && resultSet.getString(2).equals(password)) {
                return resultSet.getInt(1);
            } else {
                System.err.println("Error: Invalid email or password.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Error: Unable to authenticate user.");
        return -1;
    }

    public static boolean changePasswordAndEmail(Connection connection, String password, String email, Customer customer) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_PASSWORD_AND_EMAIL);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, customer.getCustomerId());
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Error: Unable to change password and email.");
        return false;
    }

    public static Integer getCustomerIdByEmail(Connection connection, String email) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_CUSTOMER_ID_BY_EMAIL);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Error: Unable to get customer ID by email.");
        return null;
    }
}
