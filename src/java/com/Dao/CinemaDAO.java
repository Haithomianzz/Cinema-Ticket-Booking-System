package com.Dao;

import com.Entities.Cinema;

import java.sql.Connection;
import java.util.HashMap;

public class CinemaDAO {
    private static final String GET_ALL_CINEMAS = "SELECT * FROM cinema";
    private static final String GET_CINEMA_BY_ID = "SELECT * FROM cinema WHERE cinema_id = ?";
    private static final String INSERT_CINEMA = "INSERT INTO cinema (name, location, number_of_halls, contact_info) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_CINEMA = "UPDATE cinema SET name = ?, location = ?, number_of_halls = ?, contact_info = ? WHERE cinema_id = ?";
    private static final String DELETE_CINEMA = "DELETE FROM cinema WHERE cinema_id = ?";

    public static HashMap<Integer, Cinema> getAllCinemas(Connection connection) {
        // Implementation here
    }
}