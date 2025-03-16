package com.Entities;

import java.util.ArrayList;

public class Hall {
    private static int counterID = 1;

    private int hallNumber;
    private int numberOfSeats;

    private Cinema cinema;
    private ArrayList<Seat> seats = new ArrayList<>();
    private ArrayList<Showtime> showtimes = new ArrayList<>();


    public Hall(int cinemaId, int numberOfSeats) {
        this.hallNumber = counterID++;
        this.cinemaId = cinemaId;
        this.numberOfSeats = numberOfSeats;
    }

    public int getHallNumber() { return hallNumber; }
    public int getCinemaId() { return cinemaId; }
    public int getNumberOfSeats() { return numberOfSeats; }

    public void setCinemaId(int cinemaId) { this.cinemaId = cinemaId; }
    public void setNumberOfSeats(int numberOfSeats) { this.numberOfSeats = numberOfSeats; }

    @Override
    public String toString() {
        return "\nHall Number: " + hallNumber +
                "\nCinema ID: " + cinemaId +
                "\nNumber of Seats: " + numberOfSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Hall hall = (Hall) o;
        return hallNumber == hall.hallNumber &&
                cinemaId == hall.cinemaId &&
                numberOfSeats == hall.numberOfSeats;
    }

}
