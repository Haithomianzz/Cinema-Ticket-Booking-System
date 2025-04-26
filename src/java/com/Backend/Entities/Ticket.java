package com.Backend.Entities;

import java.util.ArrayList;

public class Ticket {
    private String qrCode;
    private static int counterID = 0;
    private final int ticketId;
    private final Booking booking;
    private final Showtime showtime;
    private final Seat seat;

    public Ticket(Booking booking, Showtime showtime,Seat seat) {
        this.ticketId = counterID++;
        this.booking = booking;
        this.showtime = showtime;
        this.seat = seat;
        this.qrCode = generateQRCode();
        booking.addTicket(this);
        showtime.addTicket(this);
        seat.addTicket(this);
    }
    public Ticket(int ticketId, Booking booking, Showtime showtime, Seat seat) {
        this.ticketId = ticketId;
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
        seat.removeTicket(this);
    }
    public static void setTicketIdCounter(int counterID) {
        Ticket.counterID = counterID;
    }
    @Override
    public String toString() {
        return qrCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public static int getCounterID() {
        return counterID;
    }

    public int getTicketId() {
        return ticketId;
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
