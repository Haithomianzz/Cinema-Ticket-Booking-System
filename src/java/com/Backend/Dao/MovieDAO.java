package com.Backend.Dao;

import com.Backend.Entities.Movie;

import javax.print.attribute.ResolutionSyntax;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class MovieDAO {
    // private static final String GET_MOVIE_BY_ID = "SELECT * FROM movie WHERE movie_id = ?";
    private static final String GET_ALL_MOVIES = "SELECT * FROM movie";
    private static final String GET_MAX_MOVIE_ID = "SELECT MAX(movie_id) FROM customer";
    private static final String GET_GENRES_BY_MOVIE = "SELECT genre FROM movie_genre WHERE movie_id = ?";
    private static final String INSERT_MOVIE = "INSERT INTO movie (title, duration, language, release_date, rating, description) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_MOVIE = "UPDATE movie SET title = ?, duration = ?, language = ?, release_date = ?, rating = ?, description = ? WHERE movie_id = ?";
    private static final String DELETE_MOVIE = "DELETE FROM movie WHERE movie_id = ?";

    public static int getMaxMovieId(Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_MAX_MOVIE_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Failed to get max customer id");
        return 0;
    }
    public static HashMap<Integer, Movie> getAllMovies(Connection connection) {
        HashMap<Integer, Movie> movieMap = new HashMap<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_MOVIES);
            ResultSet movieResultSet = preparedStatement.executeQuery();
            while (movieResultSet.next()) {
                ArrayList<Movie.Genre> genres = new ArrayList<>();
                PreparedStatement genreStatement = connection.prepareStatement(GET_GENRES_BY_MOVIE);
                genreStatement.setInt(1, movieResultSet.getInt(1));
                ResultSet genreResultSet = genreStatement.executeQuery();
                while (genreResultSet.next())
                    genres.add(Movie.Genre.valueOf(genreResultSet.getString(1).toUpperCase()));
                movieMap.put(movieResultSet.getInt(1), new Movie(
                        movieResultSet.getInt(1),
                        movieResultSet.getString(2),
                        movieResultSet.getString(3),
                        movieResultSet.getFloat(4),
                        movieResultSet.getString(5),
                        movieResultSet.getInt(6),
                        movieResultSet.getString(7),
                        genres
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieMap;
    }
    public static boolean insertMovie(Connection connection, Movie movie) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_MOVIE);
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setInt(2, movie.getDuration());
            preparedStatement.setString(3, movie.getLanguage().toString().toUpperCase());
            preparedStatement.setString(4, movie.getReleaseDate().toString());
            preparedStatement.setFloat(5, movie.getRating());
            preparedStatement.setString(6, movie.getDescription());
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Failed to insert movie");
        return false;
    }
    public static boolean updateMovie(Connection connection, Movie movie) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_MOVIE);
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setInt(2, movie.getDuration());
            preparedStatement.setString(3, movie.getLanguage().toString().toUpperCase());
            preparedStatement.setString(4, movie.getReleaseDate().toString());
            preparedStatement.setFloat(5, movie.getRating());
            preparedStatement.setString(6, movie.getDescription());
            preparedStatement.setInt(7, movie.getMovieId());
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Failed to update movie");
        return false;
    }
    public static boolean deleteMovie(Connection connection, Movie movie) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_MOVIE);
            preparedStatement.setInt(1, movie.getMovieId());
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Failed to delete movie");
        return false;
    }




}