package com.Backend.Entities;

import java.util.ArrayList;
import java.util.Date;

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


    public Showtime(Movie movie, Hall hall, Date showDate, String showTime, int availableSeats) {
        this.showtimeId = counterID++;
        this.movie = movie;
        this.hall = hall;
        this.showDate = showDate;
        this.showTime = showTime;
        this.availableSeats = availableSeats;
    }

    public int getShowtimeId() { return showtimeId; }
    public int getMovieId() { return movie.getMovieId(); }
    public int getHallNumber() { return hall.getHallNumber(); }
    public Date getShowDate() { return showDate; }
    public String getShowTime() { return showTime; }
    public ArrayList<Seat> getAvailableSeats() { return seatsEmpty; }

//    public void setMovieId(int movieId) { this.movieId = movieId; }
//    public void setHallNumber(int hallNumber) { this.hallNumber = hallNumber; }
    public void setShowDate(Date showDate) { this.showDate = showDate; }
    public void setShowTime(String showTime) { this.showTime = showTime; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }
    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }
    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
    }
    @Override
    public String toString() {
        return "\nShowtime ID: " + showtimeId +
                "\nMovie ID: " + getMovieId() +
                "\nHall Number: " + getHallNumber() +
                "\nShow Date: " + showDate +
                "\nShow Time: " + showTime +
                "\nAvailable Seats: " + availableSeats;
    }

    public void addSeats(ArrayList<Seat> seats) {
        seatsEmpty.addAll(seats);
    }

    public void removeSeats(ArrayList<Seat> seats) {
        seatsEmpty.removeAll(seats);
    }
}
