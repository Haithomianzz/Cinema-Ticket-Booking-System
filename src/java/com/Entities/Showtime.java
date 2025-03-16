package com.Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Showtime {
    private static int counterID = 1;

    private int showtimeId;
    private Date showDate;
    private String showTime;
    private int availableSeats;

    private Movie movie;
    private Hall hall;
    private ArrayList<Booking> bookings = new ArrayList<>();
    private ArrayList<Ticket> tickets = new ArrayList<>();
    private ArrayList<Seat> seatsEmpty = new ArrayList<>();


    public Showtime(int movieId, int hallNumber, Date showDate, String showTime, int availableSeats) {
        this.showtimeId = counterID++;
        this.movieId = movieId;
        this.hallNumber = hallNumber;
        this.showDate = showDate;
        this.showTime = showTime;
        this.availableSeats = availableSeats;
    }

    public int getShowtimeId() { return showtimeId; }
    public int getMovieId() { return movieId; }
    public int getHallNumber() { return hallNumber; }
    public Date getShowDate() { return showDate; }
    public String getShowTime() { return showTime; }
    public ArrayList<Seat> getAvailableSeats() { return seatsEmpty; }

    public void setMovieId(int movieId) { this.movieId = movieId; }
    public void setHallNumber(int hallNumber) { this.hallNumber = hallNumber; }
    public void setShowDate(Date showDate) { this.showDate = showDate; }
    public void setShowTime(String showTime) { this.showTime = showTime; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    @Override
    public String toString() {
        return "\nShowtime ID: " + showtimeId +
                "\nMovie ID: " + movieId +
                "\nHall Number: " + hallNumber +
                "\nShow Date: " + showDate +
                "\nShow Time: " + showTime +
                "\nAvailable Seats: " + availableSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Showtime showtime = (Showtime) o;
        return showtimeId == showtime.showtimeId && movieId == showtime.movieId &&
                hallNumber == showtime.hallNumber && availableSeats == showtime.availableSeats &&
                Objects.equals(showDate, showtime.showDate) && Objects.equals(showTime, showtime.showTime);
    }

}
