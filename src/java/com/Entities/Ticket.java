package com.Entities;

import java.util.ArrayList;

public class Ticket {
    private String qrCode;

    private Booking booking;
    private Showtime showtime;
    private ArrayList<Seat> seats;
    public Ticket(Booking booking, Showtime showtime, ArrayList<Seat> seats) {
        this.booking = booking;
        this.showtime = showtime;
        this.seats = seats;
        this.qrCode = generateQRCode();
    }
    String generateQRCode() {
        return "" + showtime.getShowtimeId() + booking.getBookingID();
    }

    public int[] getSeatIds() {
        int[] seatIds = new int[seats.size()];
        for (int i = 0; i < seats.size(); i++) {
            seatIds[i] = seats.get(i).getSeatId();
        }
        return seatIds;
    }
    public String getQrCode() { return qrCode; }

    public void changeSeats(ArrayList<Seat> seats) {
        this.booking.changeSeats(seats);
        this.seats = seats;
    }
    @Override
    public String toString() {
        return "\nQR Code: " + qrCode;
    }
}
