package com.Backend.Dao;

import com.Backend.Entities.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class CustomerDAO {
    private static final String GET_ALL_CUSTOMERS = "SELECT * FROM customer";
    private static final String GET_CUSTOMER_BY_ID = "SELECT * FROM customer WHERE customer_id = ?";
    private static final String GET_MAX_CUSTOMER_ID = "SELECT MAX(customer_id) FROM customer";
    private static final String INSERT_CUSTOMER = "INSERT INTO customer (name, email, phone_number, password, membership_status) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_CUSTOMER = "UPDATE customer SET name = ?, email = ?, phone_number = ?, password = ?, membership_status = ? WHERE customer_id = ?";
    private static final String DELETE_CUSTOMER = "DELETE FROM customer WHERE customer_id = ?";

    public static HashMap<Integer, Customer> getAllCustomers(Connection connection) {
        HashMap<Integer, Customer> customerMap = new HashMap<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_CUSTOMERS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                customerMap.put(resultSet.getInt(1), new Customer(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        Customer.statusFromString(resultSet.getString(6))
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerMap;
    }
    public static Customer getCustomerById(Connection connection, int customer_id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_CUSTOMER_BY_ID);
            preparedStatement.setInt(1, customer_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Customer(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        Customer.statusFromString(resultSet.getString(6))
                );
            }
            else {
                System.err.println("No customer with id " + customer_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static int getMaxCustomerId(Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_MAX_CUSTOMER_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Failed to get max customer id");
        return -1;
    }
    public static void insertCustomer(Connection connection, Customer customer) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CUSTOMER);
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.setString(3, customer.getPhone());
            preparedStatement.setString(4, customer.getPassword());
            preparedStatement.setString(5, customer.getMembership().toString());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}