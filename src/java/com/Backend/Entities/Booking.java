package com.Backend.Entities;

import com.Frontend.Date;

import java.util.ArrayList;

public class Booking {

    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        CANCELLED
    }
    private static int counterID = 0;
    private final int bookingID;

    private double totalPrice;
    private Date bookingDate;
    private BookingStatus bookingStatus;

    // Stored Objects for Booking
    private Customer customer;
    private ArrayList<Ticket> tickets = new ArrayList<>();

    public Booking(Customer customer, double totalPrice, Date bookingDate, String bookingStatus) {
        this.bookingID = ++counterID;
        this.customer = customer;
        this.totalPrice = totalPrice;
        this.bookingDate = bookingDate;
        this.bookingStatus = BookingStatus.valueOf(bookingStatus.toUpperCase());
        customer.addBooking(this);
    }
    public Booking(int bookingID,Customer customer, double totalPrice, Date bookingDate, String bookingStatus) {
        this.bookingID = bookingID;
        this.customer = customer;
        this.totalPrice = totalPrice;
        this.bookingDate = bookingDate;
        this.bookingStatus = BookingStatus.valueOf(bookingStatus.toUpperCase());
        customer.addBooking(this);
    }
    public void editBooking(Customer customer, double totalPrice, Date bookingDate, String bookingStatus) {
        this.customer = customer != null ? customer : this.customer;
        this.totalPrice = totalPrice != 0 ? totalPrice : this.totalPrice;
        this.bookingDate = bookingDate != null ? bookingDate : this.bookingDate;
        this.bookingStatus = bookingStatus != null ? BookingStatus.valueOf(bookingStatus.toUpperCase()) : this.bookingStatus;
    }
    public int getBookingId() {
        return bookingID;
    }
    public Customer getCustomer() {
        return customer;
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
    public ArrayList<Ticket> getTickets() { return tickets; }
    public void deleteBooking() {
        customer.removeBooking(this);
    }
    public void cancelBooking() {
        bookingStatus = BookingStatus.CANCELLED;
    }
    public static void setBookingIdCounter(int counterID) {
        Booking.counterID = counterID;
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
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = BookingStatus.valueOf(bookingStatus.toUpperCase());}
    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }
    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
    }


    @Override
    public String toString() {
        return "\nBooking Transaction: " +
                "\ncustomerId= " + customer.getCustomerId() +
                "\ncustomerName= " + customer.getName() +
                "\nbookingID= " + bookingID +
                "\ntotalPrice= " + totalPrice +
                "\nbookingDate= " + bookingDate +
                "\nbookingStatus= " + bookingStatus + '\n' ;
    }
}
