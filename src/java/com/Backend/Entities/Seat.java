package com.Backend.Entities;

import java.util.ArrayList;

public class Seat {
    private static int counterID = 1;
    private final int seatId;

    private Hall hall;

    private int rowNumber;
    private int seatNumber;

    private ArrayList<Ticket> tickets = new ArrayList<>();
    public Seat(Hall hall, int rowNumber, int seatNumber) {
        this.seatId = ++counterID;
        this.hall = hall;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        hall.addSeat(this);
    }
    public Seat(int seatId, Hall hall, int rowNumber, int seatNumber) {
        this.seatId = seatId;
        this.hall = hall;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        hall.addSeat(this);
    }
    public void editSeat(int rowNumber, int seatNumber) {
        this.rowNumber = rowNumber != 0 ? rowNumber : this.rowNumber;
        this.seatNumber = seatNumber != 0 ? seatNumber : this.seatNumber;
    }

    public int getSeatId() { return seatId; }
    public Hall getHall() { return hall; }
    public int getSeatNumber() { return seatNumber; }
    public int getRowNumber() { return rowNumber; }
    public ArrayList<Ticket> getTickets() { return tickets; }
    public void removeSeat() {
        hall.removeSeat(this);
    }
    public static void setSeatIdCounter(int counterID) {
        Seat.counterID = counterID;
    }
    public void setHall(Hall hall) { this.hall = hall; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
    public void setRowNumber(int rowNumber) { this.rowNumber = rowNumber; }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }
    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
    }
    @Override
    public String toString() {
        return "\nSeat ID: " + seatId +
                "\nHall Number: " + hall.getHallNumber() +
                "\nSeat Number: " + seatNumber +
                "\nRow: " + rowNumber;
    }

}
