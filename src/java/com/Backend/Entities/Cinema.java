package com.Backend.Entities;

import java.util.ArrayList;

public class Cinema {

    private static int counterID  = 1;

    private int cinemaId;
    private String name;
    private String location;
    private int numberOfHalls;
    private String contactInfo;

    private ArrayList<Hall> halls;



    public Cinema(String name, String location, int numberOfHalls, String contactInfo) {
        this.cinemaId = counterID++;
        this.name = name;
        this.location = location;
        this.numberOfHalls = numberOfHalls;
        this.contactInfo = contactInfo;
    }
    public void addHall(Hall hall) {
        halls.add(hall);
    }
    public void removeHall(Hall hall) {
        halls.remove(hall);
    }

    public int getCinemaId() { return cinemaId; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public int getNumberOfHalls() { return numberOfHalls; }
    public String getContactInfo() { return contactInfo; }

    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
    public void setNumberOfHalls(int numberOfHalls) { this.numberOfHalls = numberOfHalls; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }


}
