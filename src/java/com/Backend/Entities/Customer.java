package com.Backend.Entities;


import java.util.ArrayList;

public class Customer {

    public enum MembershipStatus {
        REGULAR,
        PREMIUM,
        VIP,
        ADMIN
    }
    private static int counterID = 0;
    private final int customer_id;
    private String name;
    private String email;
    private String password;
    private String phone_number;
    private MembershipStatus membership;

    private ArrayList<Booking> bookings = new ArrayList<>();

    public Customer(String name, String email, String phone, String Password, MembershipStatus membership) {
        this.customer_id = ++counterID;
        this.name = name;
        this.email = email;
        this.phone_number = phone;
        this.password = Password;
        this.membership = membership;
    }
    public Customer(int customer_id, String name, String email, String phone, String Password, MembershipStatus membership) {
        this.customer_id = customer_id;
        this.name = name;
        this.email = email;
        this.phone_number = phone;
        this.password = Password;
        this.membership = membership;
    }
    public void editProfile(String name, String email, String phone, String Password, MembershipStatus membership) {
        this.name = (name != null && !name.equals(this.name)) ? name : this.name;
        this.email = (email != null && !email.equals(this.email)) ? email : this.email;
        this.phone_number = (phone != null && !phone.equals(this.phone_number)) ? phone : this.phone_number;
        this.password = (Password != null && !Password.equals(this.password)) ? Password : this.password;
        this.membership = (membership != null && !membership.equals(this.membership)) ? membership : this.membership;
    }
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }

    public int getCustomerId() {
        return customer_id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone_number;
    }
    public String getPassword() {
        return password;
    }
    public MembershipStatus getMembership() {
        return membership;
    }
    public ArrayList<Booking> getBookings() { return bookings; }

    public static void setCustomerIdCounter(Integer integer) {
        counterID = integer;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) {
        this.phone_number = phone;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setMembership(MembershipStatus membership) {
        this.membership = membership;
    }



}
