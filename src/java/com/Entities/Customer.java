package com.Entities;


import java.util.ArrayList;
import java.util.Objects;

public class Customer {
    public enum MembershipStatus {
        REGULAR,
        PREMIUM,
        VIP
    }

    private static int counterID  = 1;

    private int customer_id;
    private String name;
    private String email;
    private String phone_number;
    private String Password;
    private MembershipStatus membership;

    private ArrayList<Booking> bookings = new ArrayList<>();

    public Customer(String name, String email, String phone, String address, String Password, MembershipStatus membership) {
        this.customer_id = counterID++;
        this.name = name;
        this.email = email;
        this.phone_number = phone;
        this.Password = Password;
        this.membership = membership;
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

    public String toString() {
        return "\nCustomerServices ID: " + customer_id +
                "\nName: " + name +
                "\nEmail: " + email +
                "\nPhone: " + phone_number +
                "\nMembership: " + membership;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customer_id == customer.customer_id && Objects.equals(name, customer.name) && Objects.equals(email, customer.email) && Objects.equals(phone_number, customer.phone_number) && Objects.equals(address, customer.address) && Objects.equals(Password, customer.Password) && membership == customer.membership;
    }


}
