package com.Backend.Dao;

import com.Backend.Entities.Movie;

import javax.print.attribute.ResolutionSyntax;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MovieDAO {
    // private static final String GET_MOVIE_BY_ID = "SELECT * FROM movie WHERE movie_id = ?";
    private static final String GET_ALL_MOVIES = "SELECT movie_id, title, description, rating, language, duration, release_date, image FROM movie";
    private static final String GET_MAX_MOVIE_ID = "SELECT MAX(movie_id) FROM movie";
    private static final String GET_GENRES_BY_MOVIE = "SELECT genre FROM movie_genre WHERE movie_id = ?";
    private static final String INSERT_MOVIE = "INSERT INTO movie (title, duration, language, release_date, rating, description, image) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_MOVIE_GENRE = "INSERT INTO movie_genre (movie_id, genre) VALUES (?, ?)";
    private static final String UPDATE_MOVIE = "UPDATE movie SET title = ?, description = ?, rating = ?, language = ?, duration = ?, release_date = ?, ImageData = ? WHERE movie_id = ?";
    private static final String DELETE_MOVIE = "DELETE FROM movie WHERE movie_id = ?";
    private static final String DELETE_MOVIE_FROM_GENRE = "DELETE FROM movie_genre WHERE movie_id = ?";

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
                byte[] imageData = movieResultSet.getBytes("image");
                movieMap.put(movieResultSet.getInt(1), new Movie(
                        movieResultSet.getInt(1),
                        movieResultSet.getString(2),
                        movieResultSet.getString(3),
                        movieResultSet.getFloat(4),
                        movieResultSet.getString(5),
                        movieResultSet.getInt(6),
                        movieResultSet.getString(7),
                        genres,
                        imageData
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
            for (Movie.Genre genre : movie.getGenres()) {
                PreparedStatement genreStatement = connection.prepareStatement(INSERT_MOVIE_GENRE);
                genreStatement.setInt(1, movie.getMovieId());
                genreStatement.setString(2, genre.toString().toUpperCase());
                genreStatement.executeUpdate();
            }
            byte[] imageData = movie.getImageData();
            if (imageData != null && imageData.length > 0) {
                preparedStatement.setBytes(7, imageData);
                // Alternatively, for potentially large files (requires image data as InputStream):
                // preparedStatement.setBinaryStream(7, new ByteArrayInputStream(imageData), imageData.length);
            } else {
                // If image is optional and the column allows NULLs
                preparedStatement.setNull(7, Types.VARBINARY);
            }
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
            // Set image data (handle null)
            byte[] imageData = movie.getImageData();
            if (imageData != null && imageData.length > 0) {
                preparedStatement.setBytes(7, imageData);
                // Or: preparedStatement.setBinaryStream(7, new ByteArrayInputStream(imageData), imageData.length);
            } else {
                preparedStatement.setNull(7, Types.VARBINARY); // Allow clearing the image
            }

            // Set the movie ID for the WHERE clause
            preparedStatement.setInt(8, movie.getMovieId());
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
            preparedStatement.executeUpdate();
            PreparedStatement genreStatement = connection.prepareStatement(DELETE_MOVIE_FROM_GENRE);
            genreStatement.setInt(1, movie.getMovieId());
            genreStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Failed to delete movie");
        return false;
    }




}