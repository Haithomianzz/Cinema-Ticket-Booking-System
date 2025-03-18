package com.Entities;


import com.Dao.CustomerDAO;

import java.util.ArrayList;

public class Customer {

    public enum MembershipStatus {
        REGULAR,
        PREMIUM,
        VIP
    }
    public static MembershipStatus statusFromString(String status) {
        return switch (status.toUpperCase()) {
            case "REGULAR" -> MembershipStatus.REGULAR;
            case "PREMIUM" -> MembershipStatus.PREMIUM;
            case "VIP" -> MembershipStatus.VIP;
            default -> null;
        };
    }

    private static int counterID;

    private final int customer_id;
    private String name;
    private String email;
    private String phone_number;
    private String Password;
    private MembershipStatus membership;

    private ArrayList<Booking> bookings = new ArrayList<>();

    public Customer(String name, String email, String phone, String Password, MembershipStatus membership) {
        this.customer_id = counterID++;
        this.name = name;
        this.email = email;
        this.phone_number = phone;
        this.Password = Password;
        this.membership = membership;
    }
    public Customer(int customer_id, String name, String email, String phone, String Password, MembershipStatus membership) {
        this.customer_id = customer_id;
        this.name = name;
        this.email = email;
        this.phone_number = phone;
        this.Password = Password;
        this.membership = membership;
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
        return Password;
    }
    public MembershipStatus getMembership() {
        return membership;
    }
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
        Password = password;
    }
    public void setMembership(MembershipStatus membership) {
        this.membership = membership;
    }



}
