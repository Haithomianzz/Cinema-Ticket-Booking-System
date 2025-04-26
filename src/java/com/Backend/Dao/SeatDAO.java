package com.Backend.Dao;

import com.Backend.Entities.Hall;
import com.Backend.Entities.Seat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class SeatDAO {
    //    private static final String GET_SEAT_BY_ID = "SELECT * FROM seat WHERE seat_id = ?";
    private static final String GET_MAX_SEAT_ID = "SELECT MAX(seat_id) FROM seat";
    private static final String GET_ALL_SEATS = "SELECT * FROM seat";
    private static final String INSERT_SEAT = "INSERT INTO seat (hall_number, row_number, seat_number) VALUES (?, ?, ?)";
    private static final String UPDATE_SEAT = "UPDATE seat SET hall_number = ?, row_number = ?, seat_number = ? WHERE seat_id = ?";
    private static final String DELETE_SEAT = "DELETE FROM seat WHERE seat_id = ?";
    private static final String DELETE_SEAT_BY_HALL_ID = "DELETE FROM seat WHERE hall_number = ?";
    private static final String DECREMENT_NUMBER_OF_SEATS = "UPDATE hall SET number_of_seats = number_of_seats - 1 WHERE hall_number = ?";
    private static final String INCREMENT_NUMBER_OF_SEATS = "UPDATE hall SET number_of_seats = number_of_seats + 1 WHERE hall_number = ?";
    public static int getMaxSeatId(Connection connection){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_MAX_SEAT_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Failed to get max seat id");
        return 0;
    }
    public static HashMap<Integer, Seat> getAllSeats(Connection connection, HashMap<Integer, Hall> hallMap) {
        HashMap<Integer, Seat> seatMap = new HashMap<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_SEATS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Hall hall = hallMap.get(resultSet.getInt(2));
                Seat seat = new Seat(
                        resultSet.getInt(1),
                        hall,
                        resultSet.getInt(3),
                        resultSet.getInt(4)
                );
                hall.addSeat(seat);
                seatMap.put(seat.getSeatId(), seat);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return seatMap;
    }
    public static boolean insertSeat(Connection connection, Seat seat){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SEAT);
            preparedStatement.setInt(1, seat.getHall().getHallNumber());
            preparedStatement.setInt(2, seat.getRowNumber());
            preparedStatement.setInt(3, seat.getSeatNumber());
            preparedStatement.executeUpdate();
            PreparedStatement incrementStatement = connection.prepareStatement(INCREMENT_NUMBER_OF_SEATS);
            incrementStatement.setInt(1, seat.getHall().getHallNumber());
            incrementStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Failed to insert seat");
        return false;
    }
    public static boolean updateSeat(Connection connection, Seat seat){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SEAT);
            preparedStatement.setInt(1, seat.getHall().getHallNumber());
            preparedStatement.setInt(2, seat.getRowNumber());
            preparedStatement.setInt(3, seat.getSeatNumber());
            preparedStatement.setInt(4, seat.getSeatId());
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Failed to update seat");
        return false;
    }
    public static boolean deleteSeat(Connection connection, Seat seat){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SEAT);
            preparedStatement.setInt(1, seat.getSeatId());
            preparedStatement.executeUpdate();
            PreparedStatement decrementStatement = connection.prepareStatement(DECREMENT_NUMBER_OF_SEATS);
            decrementStatement.setInt(1, seat.getHall().getHallNumber());
            decrementStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Failed to delete seat");
        return false;
    }
    public static boolean deleteSeatByHall(Connection connection, Hall hall){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SEAT_BY_HALL_ID);
            preparedStatement.setInt(1, hall.getHallNumber());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Failed to delete seat");
        return false;
    }
}