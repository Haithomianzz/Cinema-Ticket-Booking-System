package com.Entities;

import java.util.Objects;

public class Seat {
    private static int counterID = 1;

    private int seatId;
    private int hallNumber;
    private String seatNumber;
    private String rowNumber;


    public Seat(int hallNumber, String seatNumber, String rowNumber) {
        this.seatId = counterID++;
        this.hallNumber = hallNumber;
        this.seatNumber = seatNumber;
        this.rowNumber = rowNumber;
    }

    public int getSeatId() { return seatId; }
    public int getHallNumber() { return hallNumber; }
    public String getSeatNumber() { return seatNumber; }
    public String getRowNumber() { return rowNumber; }

    public void setHallNumber(int hallNumber) { this.hallNumber = hallNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public void setRowNumber(String rowNumber) { this.rowNumber = rowNumber; }

    @Override
    public String toString() {
        return "\nSeat ID: " + seatId +
                "\nHall Number: " + hallNumber +
                "\nSeat Number: " + seatNumber +
                "\nRow: " + rowNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return seatId == seat.seatId && hallNumber == seat.hallNumber &&
                Objects.equals(seatNumber, seat.seatNumber) && Objects.equals(rowNumber, seat.rowNumber);
    }

}
