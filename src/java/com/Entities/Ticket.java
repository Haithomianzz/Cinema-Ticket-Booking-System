package com.Entities;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Objects;

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

    public int getSeatId() { return seatId; }
    public String getQrCode() { return qrCode; }

    public void setSeatId(int seatId) { this.seatId = seatId; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    @Override
    public String toString() {
        return "\nQR Code: " + qrCode;
    }
}
