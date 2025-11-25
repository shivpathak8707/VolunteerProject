package com.volunteerapp;

public class ParticipationRecord {
    private int id;
    private int volunteerId;
    private int opportunityId;
    private int hours;
    private boolean approved;

    public ParticipationRecord(int id, int volunteerId, int opportunityId, int hours, boolean approved) {
        this.id = id;
        this.volunteerId = volunteerId;
        this.opportunityId = opportunityId;
        this.hours = hours;
        this.approved = approved;
    }

    public int getId() { return id; }
    public int getVolunteerId() { return volunteerId; }
    public int getOpportunityId() { return opportunityId; }
    public int getHours() { return hours; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
}
