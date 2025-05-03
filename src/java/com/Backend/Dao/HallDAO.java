package com.Backend.Dao;

import com.Backend.Entities.Customer;
import com.Backend.Entities.Hall;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class HallDAO {
//    private static final String GET_HALL_BY_ID = "SELECT * FROM hall WHERE hall_number = ?";
    private static final String GET_ALL_HALLS = "SELECT hall_number FROM hall";
    private static final String GET_MAX_HALL_ID = "SELECT MAX(hall_number) FROM hall";
    private static final String DELETE_HALL = "DELETE FROM hall WHERE hall_number = ?";
    private static final String INSERT_HALL_PROCEDURE = "EXEC createHallseats ?";

    public static int getMaxHallId(Connection connection){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_MAX_HALL_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Failed to get max hall id");
        return 0;
    }
    public static HashMap<Integer,Hall> getAllHalls(Connection connection) {
        HashMap<Integer, Hall> hallMap = new HashMap<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_HALLS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                hallMap.put(resultSet.getInt(1), new Hall(resultSet.getInt(1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hallMap;
    }
    public static boolean insertHall(Connection connection, Hall hall, int numberOfSeats) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_HALL_PROCEDURE);
            preparedStatement.setInt(1, numberOfSeats);
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Failed to insert hall");
        return false;
    }
    public static boolean deleteHall(Connection connection, Hall hall) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_HALL);
            preparedStatement.setInt(1, hall.getHallNumber());
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Failed to delete hall");
        return false;
    }
}