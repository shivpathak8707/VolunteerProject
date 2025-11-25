package com.volunteerapp;

import java.time.LocalDate;
import java.util.List;

public class MockDataSeeder {

    public static void seed(List<User> users,
                            List<VolunteerOpportunity> opportunities,
                            List<ParticipationRecord> participationRecords) {

        Admin admin1 = new Admin(1, "Admin One", "admin1@example.com");
        Organization org1 = new Organization(2, "Org User", "org@example.com", "Helping Hands");
        Volunteer vol1 = new Volunteer(3, "Volunteer One", "vol1@example.com");
        Volunteer vol2 = new Volunteer(4, "Volunteer Two", "vol2@example.com");

        users.add(admin1);
        users.add(org1);
        users.add(vol1);
        users.add(vol2);

        VolunteerOpportunity opp1 = new VolunteerOpportunity(
                1, "Tree Plantation", "Plant trees in city park",
                LocalDate.now().plusDays(7), "City Park",
                OpportunityStatus.APPROVED, org1.getId()
        );

        VolunteerOpportunity opp2 = new VolunteerOpportunity(
                2, "Beach Cleanup", "Clean the local beach",
                LocalDate.now().plusDays(10), "Beach Area",
                OpportunityStatus.PENDING_APPROVAL, org1.getId()
        );

        opportunities.add(opp1);
        opportunities.add(opp2);

        ParticipationRecord pr1 = new ParticipationRecord(1, vol1.getId(), opp1.getId(), 5, true);
        ParticipationRecord pr2 = new ParticipationRecord(2, vol2.getId(), opp1.getId(), 3, true);

        participationRecords.add(pr1);
        participationRecords.add(pr2);
    }
}