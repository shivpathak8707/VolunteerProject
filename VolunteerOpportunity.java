package com.volunteerapp;

import java.time.LocalDate;

public class VolunteerOpportunity {
    private int id;
    private String title;
    private String description;
    private LocalDate date;
    private String location;
    private OpportunityStatus status;
    private int organizationId;

    public VolunteerOpportunity(int id, String title, String description,
                                LocalDate date, String location,
                                OpportunityStatus status, int organizationId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.status = status;
        this.organizationId = organizationId;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }
    public String getLocation() { return location; }
    public OpportunityStatus getStatus() { return status; }
    public void setStatus(OpportunityStatus status) { this.status = status; }
    public int getOrganizationId() { return organizationId; }
}