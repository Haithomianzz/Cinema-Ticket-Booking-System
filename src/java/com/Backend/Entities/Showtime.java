package com.Backend.Entities;

import java.time.LocalDate;
import java.util.ArrayList;
import com.Frontend.Date;

public class Showtime {
    private static int counterID = 0;

    private final int showtimeId;

    private Movie movie;
    private Hall hall;

    private Date showDate;
    private String showTime;
    private int pricePerSeat;

    private ArrayList<Ticket> tickets = new ArrayList<>();
    private ArrayList<Seat> seatsAvailable;


    public Showtime(Movie movie, Hall hall, Date showDate, String showTime, int pricePerSeat) {
        this.showtimeId = ++counterID;
        this.movie = movie;
        this.hall = hall;
        this.seatsAvailable = (hall.getSeats().isEmpty()) ? new ArrayList<>(hall.getSeats()) : hall.getSeats();
        this.showDate = showDate;
        this.showTime = showTime;
        this.pricePerSeat = pricePerSeat;
        hall.addShowtime(this);
        movie.addShowtime(this);
    }
    public Showtime(int showtimeId, Movie movie, Hall hall, String showDate, String showTime, int pricePerSeat) {
        this.showtimeId = showtimeId;
        this.movie = movie;
        this.hall = hall;
        this.seatsAvailable = (hall.getSeats().isEmpty()) ? new ArrayList<>(hall.getSeats()) : hall.getSeats();
        this.showDate = new Date(showDate);
        this.showTime = showTime;
        this.pricePerSeat = pricePerSeat;
        hall.addShowtime(this);
        movie.addShowtime(this);
    }
    public void editShowtime(Date showDate, String showTime, int pricePerSeat) {
        this.showDate = showDate != null ? showDate : this.showDate;
        this.showTime = showTime != null ? showTime : this.showTime;
        this.pricePerSeat = pricePerSeat != 0 ? pricePerSeat : this.pricePerSeat;
    }

    public int getShowtimeId() { return showtimeId; }
    public Movie getMovie() { return movie; }
    public Hall getHall() { return hall; }
    public Date getShowDate() { return showDate; }
    public String getShowTime() { return showTime; }
    public int getPricePerSeat() { return pricePerSeat; }
    public ArrayList<Ticket> getTickets() { return tickets; }


    public ArrayList<Seat> getAvailableSeats() { return seatsAvailable; }
    public void cancelShowtime() {
        hall.removeShowtime(this);
        movie.removeShowtime(this);
    }
//    public void setMovieId(int movieId) { this.movieId = movieId; }
//    public void setHallNumber(int hallNumber) { this.hallNumber = hallNumber; }
    public static void setShowtimeIdCounter(int counterID) {
        Showtime.counterID = counterID;
    }
    public void setShowDate(Date showDate) { this.showDate = showDate; }
    public void setShowTime(String showTime) { this.showTime = showTime; }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }
    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
    }
    public void addAvailableSeat(Seat seat) {
        seatsAvailable.add(seat);
    }
    public void reserveSeat(Seat seat) {
        seatsAvailable.remove(seat);
    }
    @Override
    public String toString() {
        return "\nShowtime ID: " + showtimeId +
                "\nMovie ID: " + movie.getMovieId() +
                "\nHall Number: " + hall.getHallNumber() +
                "\nShow Date: " + showDate +
                "\nShow Time: " + showTime;
    }

}
