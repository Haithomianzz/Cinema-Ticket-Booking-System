package com.Backend.Entities;

import java.util.ArrayList;

public class Hall {
    private static int counterID = 1;

    private int hallNumber;
    private int numberOfSeats;

    private Cinema cinema;
    private ArrayList<Seat> seats = new ArrayList<>();
    private ArrayList<Showtime> showtimes = new ArrayList<>();


    public Hall(Cinema cinema, int numberOfSeats) {
        this.cinema = cinema;
        this.hallNumber = counterID++;
        this.numberOfSeats = numberOfSeats;
    }

    public int getHallNumber() { return hallNumber; }
    public int getCinemaId() { return cinema.getCinemaId(); }
    public int getNumberOfSeats() { return numberOfSeats; }

    public void setCinema(Cinema cinema) { this.cinema = cinema; }
    public void setNumberOfSeats(int numberOfSeats) { this.numberOfSeats = numberOfSeats; }

    @Override
    public String toString() {
        return "\nHall Number: " + hallNumber +
                "\nCinema ID: " + cinema.getCinemaId() +
                "\nNumber of Seats: " + numberOfSeats;
    }
}
