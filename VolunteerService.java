package com.volunteerapp;

import java.util.ArrayList;
import java.util.List;

public class VolunteerService {

    private final List<VolunteerOpportunity> opportunities;
    private final List<ParticipationRecord> participationRecords;

    public VolunteerService(List<VolunteerOpportunity> opportunities,
                            List<ParticipationRecord> participationRecords) {
        this.opportunities = opportunities;
        this.participationRecords = participationRecords;
    }

    public List<VolunteerOpportunity> getAvailableOpportunities() {
        List<VolunteerOpportunity> approved = new ArrayList<>();
        for (VolunteerOpportunity opp : opportunities) {
            if (opp.getStatus() == OpportunityStatus.APPROVED) {
                approved.add(opp);
            }
        }
        return approved;
    }

    public void signUpForOpportunity(Volunteer volunteer, VolunteerOpportunity opportunity) {
        int newId;
        synchronized (participationRecords) {
            newId = participationRecords.size() + 1;
        }
        ParticipationRecord record =
                new ParticipationRecord(newId, volunteer.getId(), opportunity.getId(), 0, false);
        synchronized (participationRecords) {
            participationRecords.add(record);
        }
    }

    public List<ParticipationRecord> getParticipationForVolunteer(int volunteerId) {
        List<ParticipationRecord> result = new ArrayList<>();
        synchronized (participationRecords) {
            for (ParticipationRecord pr : participationRecords) {
                if (pr.getVolunteerId() == volunteerId) {
                    result.add(pr);
                }
            }
        }
        return result;
    }
}
