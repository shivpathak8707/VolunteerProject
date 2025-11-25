package com.volunteerapp;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class MainFrame extends JFrame {

    private final List<User> users = new ArrayList<>();
    private final List<VolunteerOpportunity> opportunities = new ArrayList<>();
    private final List<ParticipationRecord> participationRecords = new ArrayList<>();
    private StatsRefresherThread statsThread;

    public MainFrame() {
        setTitle("Volunteer Management Platform - Desktop");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        MockDataSeeder.seed(users, opportunities, participationRecords);

        MessagingService messagingService = new MessagingService();
        VolunteerService volunteerService = new VolunteerService(opportunities, participationRecords);
        OrganizationService orgService = new OrganizationService(opportunities, participationRecords);
        AdminService adminService = new AdminService(users, opportunities);

        // For demo: pick first admin, first org, first volunteer from mock list
        Admin admin = null;
        Organization org = null;
        Volunteer vol = null;

        for (User u : users) {
            if (u.getRole() == UserRole.ADMIN && admin == null)
                admin = (Admin) u;
            if (u.getRole() == UserRole.ORGANIZATION && org == null)
                org = (Organization) u;
            if (u.getRole() == UserRole.VOLUNTEER && vol == null)
                vol = (Volunteer) u;
        }

        JTabbedPane mainTabs = new JTabbedPane();
        mainTabs.setBackground(new Color(220, 20, 60));

        AdminDashboardPanel adminPanel = new AdminDashboardPanel(adminService);
        OrganizationDashboardPanel orgPanel = new OrganizationDashboardPanel(org, orgService, messagingService, users);
        VolunteerDashboardPanel volPanel = new VolunteerDashboardPanel(vol, volunteerService, messagingService, users);

        mainTabs.addTab("Admin Dashboard", adminPanel);
        mainTabs.addTab("Organization Dashboard", orgPanel);
        mainTabs.addTab("Volunteer Dashboard", volPanel);

        add(mainTabs, BorderLayout.CENTER);

        statsThread = new StatsRefresherThread(participationRecords, volPanel.getStatsLabel());
        statsThread.start();
    }

    @Override
    public void dispose() {
        if (statsThread != null) {
            statsThread.stopRunning();
        }
        super.dispose();
    }
}