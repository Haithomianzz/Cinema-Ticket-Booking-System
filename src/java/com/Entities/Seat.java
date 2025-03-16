package com.Entities;

import java.util.ArrayList;

public class Seat {

    private int seatId;
    private String seatNumber;
    private String rowNumber;

    private Hall hall;
    private ArrayList<Ticket> tickets = new ArrayList<>();
    public Seat(Hall hall,int seatId, String seatNumber, String rowNumber) {
        this.hall = hall;
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.rowNumber = rowNumber;
    }

    public int getSeatId() { return seatId; }
    public int getHallNumber() { return hall.getHallNumber(); }
    public String getSeatNumber() { return seatNumber; }
    public String getRowNumber() { return rowNumber; }

    public void setHall(Hall hall) { this.hall = hall; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public void setRowNumber(String rowNumber) { this.rowNumber = rowNumber; }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }
    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
    }
    @Override
    public String toString() {
        return "\nSeat ID: " + seatId +
                "\nHall Number: " + getHallNumber() +
                "\nSeat Number: " + seatNumber +
                "\nRow: " + rowNumber;
    }

}
