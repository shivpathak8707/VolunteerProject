package com.volunteerapp;

public class Volunteer extends User {

    private int totalHours;

    public Volunteer(int id, String name, String email) {
        super(id, name, email);
        this.totalHours = 0;
    }

    @Override
    public UserRole getRole() {
        return UserRole.VOLUNTEER;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void addHours(int hours) {
        this.totalHours += hours;
    }

    @Override
    public String toString() {
        return name + " (Volunteer)";
    }
}