package com.Backend.Entities;

import com.client.Date;

import java.util.ArrayList;
import java.util.Arrays;

public class Booking {


    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        CANCELLED
    }
    private static int counterID = 1;

    private int bookingID;
    private Date bookingDate;
    private double totalPrice;
    private BookingStatus bookingStatus;

    // Stored Objects for Booking
    private Showtime showtime;
    private Customer customer;

    private ArrayList<Seat> seats;
    private Payment payment;
    private Ticket ticket;

    public Booking(Customer customer, Showtime showtime, int numSeats, double totalPrice, Date bookingDate,
                   BookingStatus bookingStatus, Payment.PaymentMethod paymentMethod, String transactionId,
                   String paymentDate, Payment.PaymentStatus PaymentStatus) {
        this.bookingID = counterID++;
        this.customer = customer;
        this.showtime = showtime;
        this.totalPrice = totalPrice;
        this.bookingDate = bookingDate;
        this.bookingStatus = bookingStatus;
        if (reserveSeats(numSeats)) {
            this.ticket = new Ticket(this, showtime, seats);
            this.payment = new Payment(this,paymentMethod, transactionId, paymentDate, PaymentStatus);
        }
    }

    public int getBookingId() {
        return bookingID;
    }
    public int getCustomerId() {
        return customer.getCustomerId();
    }
    public int getShowtimeId() {
        return showtime.getShowtimeId();
    }
    public double getTotalPrice() {
        return totalPrice;
    }
    public Date getBookingDate() {
        return bookingDate;
    }
    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }
    public Showtime getShowtime() { return showtime;}
    public ArrayList<Seat> getSeats() { return seats; }
    public Payment getPayment() { return payment; }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }
    public void setBookingDate(String bookingDate) { this.bookingDate.setDate(bookingDate); }
    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime.removeBooking(this);
        this.showtime = showtime;
    }
    boolean reserveSeats(int numSeats) {
        if (numSeats > showtime.getAvailableSeats().size()) {
            System.out.println("Not enough seats available for booking");
            return false;
        }
        while (numSeats > 0) {
            this.seats.add(this.showtime.getAvailableSeats().getFirst());
            this.showtime.getAvailableSeats().removeFirst();
            numSeats--;
        }
        return true;
    }
    public void addSeats(ArrayList<Seat> seats) {
        this.seats.addAll(seats);

    }
    public void changeSeats(ArrayList<Seat> seats) {
        this.seats.forEach(seat -> seat.removeTicket(this.ticket));
        this.seats = seats;

    }

    public void confirmBooking() {
        this.bookingStatus = BookingStatus.CONFIRMED;
    }
    public void cancelBooking() {
        this.bookingStatus = BookingStatus.CANCELLED;
        this.showtime.addSeats(this.seats);
        this.payment.refund();
    }
    @Override
    public String toString() {
        return "\nBooking transaction " +
                "\ncustomerId=" + customer.getCustomerId() +
                "\nbookingID=" + bookingID +
                "\nshowtimeID=" + showtime.getShowtimeId() +
                "\nseatIDs=" + Arrays.toString(ticket.getSeatIds()) +
                "\ntransactionID=" + payment.getTransactionId() +
                "\ntotalPrice=" + totalPrice +
                "\nbookingDate='" + bookingDate +
                "\nbookingStatus='" + bookingStatus ;
    }
}
