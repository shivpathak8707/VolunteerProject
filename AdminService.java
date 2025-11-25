package com.volunteerapp;

import java.util.List;

public class AdminService {

    private final List<User> users;
    private final List<VolunteerOpportunity> opportunities;

    public AdminService(List<User> users, List<VolunteerOpportunity> opportunities) {
        this.users = users;
        this.opportunities = opportunities;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getAllUsers() { return users; }

    public List<VolunteerOpportunity> getAllOpportunities() { return opportunities; }

    public void approveOpportunity(int opportunityId) {
        for (VolunteerOpportunity opp : opportunities) {
            if (opp.getId() == opportunityId) {
                opp.setStatus(OpportunityStatus.APPROVED);
            }
        }
    }

    public void rejectOpportunity(int opportunityId) {
        for (VolunteerOpportunity opp : opportunities) {
            if (opp.getId() == opportunityId) {
                opp.setStatus(OpportunityStatus.REJECTED);
            }
        }
    }
}