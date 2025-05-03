package com.Backend.Dao;

import com.Backend.Entities.Hall;
import com.Backend.Entities.Movie;
import com.Backend.Entities.Seat;
import com.Backend.Entities.Showtime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class ShowtimeDAO {
    private static final String GET_ALL_SHOWTIMES = "SELECT showtime_id, movie_id, hall_number, show_date, show_time, price_per_seat FROM showtime";
    private static final String GET_MAX_SHOWTIME_ID = "SELECT MAX(showtime_id) FROM showtime";
    private static final String GET_RESERVED_SEATS = "SELECT showtime_id, seat_id FROM show_seats WHERE status = 'RESERVED'";
    private static final String INSERT_SHOWTIME = "INSERT INTO showtime (movie_id, hall_number, show_date, show_time, price_per_seat) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SHOWTIME = "UPDATE showtime SET movie_id = ?, hall_number = ?, show_date = ?, show_time = ?, price_per_seat = ? WHERE showtime_id = ?";
    private static final String DELETE_SHOWTIME = "DELETE FROM showtime WHERE showtime_id = ?";
    private static final String DELETE_SHOW_SEATS = "DELETE FROM show_seats WHERE showtime_id = ?";

    public static int getMaxShowtimeId(Connection connection) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_MAX_SHOWTIME_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Error: Unable to retrieve max showtime ID.");
        return 0;
    }
    public static HashMap<Integer, Showtime> getAllShowtimes(Connection connection, HashMap<Integer, Movie> movieMap,
                                                             HashMap<Integer, Hall> hallMap) {
        HashMap<Integer, Showtime> showtimeMap = new HashMap<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_SHOWTIMES);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Movie movie = movieMap.get(resultSet.getInt(2));
                Hall hall = hallMap.get(resultSet.getInt(3));
                Showtime showtime = new Showtime(resultSet.getInt(1), movie, hall, resultSet.getString(4),
                        resultSet.getString(5), resultSet.getInt(6));
                showtimeMap.put(resultSet.getInt(1), showtime);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return showtimeMap;
    }
    public static boolean allocateSeats(Connection connection, HashMap<Integer,Showtime> showtimeMap,
                                     HashMap<Integer, Seat> seatMap) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(GET_RESERVED_SEATS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int showtimeId = resultSet.getInt(1);
                int seatId = resultSet.getInt(2);
                Showtime showtime = showtimeMap.get(showtimeId);
                Seat seat = seatMap.get(seatId);
                showtime.reserveSeat(seat);
            }
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.err.println("Error: Unable to allocate seats.");
        return false;
    }
    public static boolean insertShowtime(Connection connection, Showtime showtime){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SHOWTIME);
            preparedStatement.setInt(1, showtime.getMovie().getMovieId());
            preparedStatement.setInt(2, showtime.getHall().getHallNumber());
            preparedStatement.setString(3, showtime.getShowDate().toString());
            preparedStatement.setString(4, showtime.getShowTime());
            preparedStatement.setInt(5, showtime.getPricePerSeat());
            return preparedStatement.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        System.err.println("Error: Unable to insert showtime.");
        return false;
    }
    public static boolean updateShowtime(Connection connection, Showtime showtime){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SHOWTIME);
            preparedStatement.setInt(1, showtime.getMovie().getMovieId());
            preparedStatement.setInt(2, showtime.getHall().getHallNumber());
            preparedStatement.setString(3, showtime.getShowDate().toString());
            preparedStatement.setString(4, showtime.getShowTime());
            preparedStatement.setInt(5, showtime.getPricePerSeat());
            preparedStatement.setInt(6, showtime.getShowtimeId());
            return preparedStatement.executeUpdate() > 0;

        }catch (Exception e){
            e.printStackTrace();
        }
        System.err.println("Error: Unable to update showtime.");
        return false;
    }
    public static boolean deleteShowtime(Connection connection, Showtime showtime){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SHOWTIME);
            preparedStatement.setInt(1, showtime.getShowtimeId());
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(DELETE_SHOW_SEATS);
            preparedStatement.setInt(1, showtime.getShowtimeId());
            preparedStatement.executeUpdate();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        System.err.println("Error: Unable to delete showtime.");
        return false;
    }

}