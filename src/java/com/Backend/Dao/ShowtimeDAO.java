package com.Backend.Dao;

import com.Backend.Entities.Hall;
import com.Backend.Entities.Movie;
import com.Backend.Entities.Showtime;
import org.javatuples.Pair;

import java.sql.Connection;
import java.util.HashMap;

public class ShowtimeDAO {
    private static final String GET_ALL_SHOWTIMES = "SELECT * FROM showtime";
    private static final String GET_SHOWTIME_BY_ID = "SELECT * FROM showtime WHERE showtime_id = ?";
    private static final String INSERT_SHOWTIME = "INSERT INTO showtime (movie_id, hall_number, show_date, show_time, available_seats) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SHOWTIME = "UPDATE showtime SET movie_id = ?, hall_number = ?, show_date = ?, show_time = ?, available_seats = ? WHERE showtime_id = ?";
    private static final String DELETE_SHOWTIME = "DELETE FROM showtime WHERE showtime_id = ?";

    public static HashMap<Integer, Showtime> getAllShowtimes(Connection connection, HashMap<Integer, Movie> movieMap, HashMap<Pair<Integer, Integer>, Hall> hallMap) {
        // Implementation here
    }
}