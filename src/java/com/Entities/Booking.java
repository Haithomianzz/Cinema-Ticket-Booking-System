package com.Entities;

import com.company.Date;

import java.util.ArrayList;

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
                   double amount, String paymentDate, Payment.PaymentStatus PaymentStatus) {
        this.bookingID = counterID++;
        this.customer = customer;
        this.showtime = showtime;
        this.totalPrice = totalPrice;
        this.bookingDate = bookingDate;
        this.bookingStatus = bookingStatus;
        if (reserveSeats(numSeats)) {
            this.ticket = new Ticket(this, showtime, seats);
            this.payment = new Payment(paymentMethod, transactionId, amount, paymentDate, PaymentStatus);
        }
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

    public int getBookingID() {
        return bookingID;
    }
    public int getCustomerId() {
        return customerId;
    }
    public int getShowtimeID() {
        return showtimeID;
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
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public void setShowtimeID(int showtimeID) {
        this.showtimeID = showtimeID;
    }
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

    @Override
    public String toString() {
        return "\nBooking transaction " +
                "\ncustomerId=" + customerId +
                "\nbookingID=" + bookingID +
                "\nticketID=" + ticket.getTicketId() +
                "\nshowtimeID=" + showtimeID +
                "\nseatID=" + ticket.getSeatId() +
                "\ntransactionID=" + payment.getTransactionId() +
                "\ntotalPrice=" + totalPrice +
                "\nbookingDate='" + bookingDate +
                "\nbookingStatus='" + bookingStatus ;
    }
}
