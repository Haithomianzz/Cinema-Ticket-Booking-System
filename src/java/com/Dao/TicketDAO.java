package com.Dao;

import com.Entities.Seat;
import com.Entities.Showtime;
import com.Entities.Ticket;

import java.sql.Connection;
import java.util.HashMap;

public class TicketDAO {
    private static final String GET_ALL_TICKETS = "SELECT * FROM ticket";
    private static final String GET_TICKET_BY_ID = "SELECT * FROM ticket WHERE ticket_id = ?";
    private static final String INSERT_TICKET = "INSERT INTO ticket (showtime_id, seat_id, qr_code) VALUES (?, ?, ?)";
    private static final String UPDATE_TICKET = "UPDATE ticket SET showtime_id = ?, seat_id = ?, qr_code = ? WHERE ticket_id = ?";
    private static final String DELETE_TICKET = "DELETE FROM ticket WHERE ticket_id = ?";

    public static HashMap<Integer, Ticket> getAllTickets(Connection connection, HashMap<Integer, Showtime> showtimeMap, HashMap<Integer, Seat> seatMap) {
        // Implementation here
    }
}