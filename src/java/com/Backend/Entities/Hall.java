package com.Backend.Entities;

import java.util.ArrayList;

public class Hall {
    private static int counterID = 0;

    private final int hallNumber;
    private int numberOfSeats;

    private ArrayList<Seat> seats = new ArrayList<>();
    private ArrayList<Showtime> showtimes = new ArrayList<>();

    public Hall() {
        this.hallNumber = ++counterID;
        this.numberOfSeats = 0;
    }
    public Hall(int hallNumber) {
        this.hallNumber = hallNumber;
        this.numberOfSeats = 0;
    }
    public static int getCounterID() { return counterID; }
    public int getHallNumber() { return hallNumber; }
    public int getNumberOfSeats() { return numberOfSeats; }
    public ArrayList<Seat> getSeats() { return seats; }
    public ArrayList<Showtime> getShowtimes() { return showtimes; }


    public void setNumberOfSeats(int numberOfSeats) { this.numberOfSeats = numberOfSeats; }
    public void addSeat(Seat seat) {
        seats.add(seat);
        numberOfSeats++;
    }
    public void removeSeat(Seat seat) {
        seats.remove(seat);
        numberOfSeats--;
    }
    public void addShowtime(Showtime showtime) {
        showtimes.add(showtime);
    }
    public void removeShowtime(Showtime showtime) {
        showtimes.remove(showtime);
    }

    public static void setHallIdCounter(int counterID) {
        Hall.counterID = counterID;
    }

    @Override
    public String toString() {
        return "\nHall Number: " + hallNumber +
                "\nNumber of Seats: " + numberOfSeats;
    }
}
