package com.Backend.Dao;

import com.Backend.Entities.Movie;

import java.sql.Connection;
import java.util.HashMap;

public class MovieDAO {
    private static final String GET_ALL_MOVIES = "SELECT * FROM movie";
    private static final String GET_MOVIE_BY_ID = "SELECT * FROM movie WHERE movie_id = ?";
    private static final String INSERT_MOVIE = "INSERT INTO movie (title, genre, duration, language, release_date, rating, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_MOVIE = "UPDATE movie SET title = ?, genre = ?, duration = ?, language = ?, release_date = ?, rating = ?, description = ? WHERE movie_id = ?";
    private static final String DELETE_MOVIE = "DELETE FROM movie WHERE movie_id = ?";

    public static HashMap<Integer, Movie> getAllMovies(Connection connection) {
        // Implementation here
    }
}