package com.Backend.Dao;

import com.Backend.Entities.Hall;
import com.Backend.Entities.Seat;
import org.javatuples.Pair;

import java.sql.Connection;
import java.util.HashMap;

public class SeatDAO {
    private static final String GET_ALL_SEATS = "SELECT * FROM seat";
    private static final String GET_SEAT_BY_ID = "SELECT * FROM seat WHERE seat_id = ?";
    private static final String INSERT_SEAT = "INSERT INTO seat (seat_number, hall_number, row_number) VALUES (?, ?, ?)";
    private static final String UPDATE_SEAT = "UPDATE seat SET seat_number = ?, hall_number = ?, row_number = ? WHERE seat_id = ?";
    private static final String DELETE_SEAT = "DELETE FROM seat WHERE seat_id = ?";

    public static HashMap<Integer, Seat> getAllSeats(Connection connection, HashMap<Pair<Integer, Integer>, Hall> hallMap) {
        // Implementation here
    }
}