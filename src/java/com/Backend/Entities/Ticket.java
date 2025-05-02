package com.Backend.Entities;

import java.util.ArrayList;

public class Ticket {
    private String qrCode;
    private final Booking booking;
    private final Showtime showtime;
    private final Seat seat;

    public Ticket(Booking booking, Showtime showtime,Seat seat) {
        this.booking = booking;
        this.showtime = showtime;
        this.seat = seat;
        this.qrCode = generateQRCode();
        booking.addTicket(this);
        showtime.addTicket(this);
        seat.addTicket(this);
    }
    public String generateQRCode() {
        return "QR-" + booking.getBookingId() + "-" + showtime.getShowtimeId() + "-" + seat.getSeatNumber();
    }
    public void cancelTicket() {
        booking.removeTicket(this);
        showtime.removeTicket(this);
        showtime.getAvailableSeats().add(seat);
        seat.removeTicket(this);
    }
    @Override
    public String toString() {
        return qrCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public Booking getBooking() {
        return booking;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public Seat getSeat() {
        return seat;
    }
}
