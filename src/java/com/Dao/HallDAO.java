package com.Dao;

import com.Entities.Cinema;
import com.Entities.Hall;
import org.javatuples.Pair;
import java.sql.Connection;
import java.util.HashMap;

public class HallDAO {
    private static final String GET_ALL_HALLS = "SELECT * FROM hall";
    private static final String GET_HALL_BY_ID = "SELECT * FROM hall WHERE hall_number = ?";
    private static final String INSERT_HALL = "INSERT INTO hall (hall_number, cinema_id, number_of_seats) VALUES (?, ?, ?)";
    private static final String UPDATE_HALL = "UPDATE hall SET hall_number = ?, cinema_id = ?, number_of_seats = ? WHERE hall_number = ?";
    private static final String DELETE_HALL = "DELETE FROM hall WHERE hall_number = ?";

    public static HashMap<Pair<Integer/* CinemaID */,Integer/* HallID*/>,Hall> getAllHalls(Connection connection, HashMap<Integer, Cinema> cinemaMap) {
        // Implementation here
    }
}