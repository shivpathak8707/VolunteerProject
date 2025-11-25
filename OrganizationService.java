package com.volunteerapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrganizationService {

    private final List<VolunteerOpportunity> opportunities;
    private final List<ParticipationRecord> participationRecords;

    public OrganizationService(List<VolunteerOpportunity> opportunities,
                               List<ParticipationRecord> participationRecords) {
        this.opportunities = opportunities;
        this.participationRecords = participationRecords;
    }

    public VolunteerOpportunity postOpportunity(int orgId, String title, String description,
                                                LocalDate date, String location) {
        int id = opportunities.size() + 1;
        VolunteerOpportunity opp = new VolunteerOpportunity(
                id, title, description, date, location, OpportunityStatus.PENDING_APPROVAL, orgId
        );
        opportunities.add(opp);
        return opp;
    }

    public List<ParticipationRecord> getParticipationForOpportunity(int opportunityId) {
        List<ParticipationRecord> result = new ArrayList<>();
        synchronized (participationRecords) {
            for (ParticipationRecord pr : participationRecords) {
                if (pr.getOpportunityId() == opportunityId) {
                    result.add(pr);
                }
            }
        }
        return result;
    }

    public List<VolunteerOpportunity> getOpportunitiesByOrg(int orgId) {
        List<VolunteerOpportunity> result = new ArrayList<>();
        for (VolunteerOpportunity opp : opportunities) {
            if (opp.getOrganizationId() == orgId) {
                result.add(opp);
            }
        }
        return result;
    }
}
