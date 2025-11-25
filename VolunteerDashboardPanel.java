package com.volunteerapp;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VolunteerDashboardPanel extends JPanel {

    private final Volunteer volunteer;
    private final VolunteerService volunteerService;
    private final MessagingService messagingService;
    private final List<User> allUsers;
    private JTable availableTable;
    private JTable mySignupsTable;
    private JLabel statsLabel;

    public VolunteerDashboardPanel(Volunteer volunteer,
                                   VolunteerService volunteerService,
                                   MessagingService messagingService,
                                   List<User> allUsers) {
        this.volunteer = volunteer;
        this.volunteerService = volunteerService;
        this.messagingService = messagingService;
        this.allUsers = allUsers;
        initUI();
        loadAvailableOpportunities();
        loadMySignups();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(220, 20, 60));

        JTabbedPane tabs = new JTabbedPane();

        JPanel availablePanel = createAvailableOpportunitiesPanel();
        JPanel mySignupsPanel = createMySignupsPanel();
        JPanel hoursPanel = createHoursPanel();
        JPanel interactionPanel = createInteractionPanel();

        tabs.addTab("Available Opportunities", availablePanel);
        tabs.addTab("My Sign-Ups", mySignupsPanel);
        tabs.addTab("Hours Tracking", hoursPanel);
        tabs.addTab("Interact with Orgs", interactionPanel);

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createAvailableOpportunitiesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 215, 0));

        String[] cols = {"ID", "Title", "Date", "Location"};
        availableTable = new JTable(new DefaultTableModel(cols, 0));
        JScrollPane scroll = new JScrollPane(availableTable);

        JButton signUpBtn = new JButton("Sign Up");
        signUpBtn.addActionListener(e -> {
            int row = availableTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an opportunity");
                return;
            }
            int oppId = (int) availableTable.getValueAt(row, 0);
            List<VolunteerOpportunity> list = volunteerService.getAvailableOpportunities();
            VolunteerOpportunity selected = null;
            for (VolunteerOpportunity opp : list) {
                if (opp.getId() == oppId) {
                    selected = opp;
                    break;
                }
            }
            if (selected != null) {
                volunteerService.signUpForOpportunity(volunteer, selected);
                JOptionPane.showMessageDialog(this, "Sign-up successful");
                loadMySignups();
            }
        });

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(signUpBtn, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createMySignupsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 215, 0));

        String[] cols = {"Participation ID", "Opportunity ID", "Hours", "Approved"};
        mySignupsTable = new JTable(new DefaultTableModel(cols, 0));
        JScrollPane scroll = new JScrollPane(mySignupsTable);

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createHoursPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 230, 128));

        statsLabel = new JLabel("Loading stats...");
        panel.add(statsLabel, BorderLayout.NORTH);

        return panel;
    }

    private JPanel createInteractionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 215, 0));

        JTextArea inboxArea = new JTextArea();
        inboxArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(inboxArea);

        JPanel sendPanel = new JPanel();
        sendPanel.setBackground(new Color(255, 230, 128));
        JComboBox<User> orgCombo = new JComboBox<>();
        for (User u : allUsers) {
            if (u.getRole() == UserRole.ORGANIZATION) {
                orgCombo.addItem(u);
            }
        }

        JTextField messageField = new JTextField(20);
        JButton sendBtn = new JButton("Send");

        sendBtn.addActionListener(e -> {
            User org = (User) orgCombo.getSelectedItem();
            if (org == null) return;
            try {
                messagingService.sendMessage(volunteer, org, messageField.getText());
                JOptionPane.showMessageDialog(this, "Message sent");
                messageField.setText("");
            } catch (AppException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        sendPanel.add(new JLabel("Organization:"));
        sendPanel.add(orgCombo);
        sendPanel.add(messageField);
        sendPanel.add(sendBtn);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(sendPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void loadAvailableOpportunities() {
        DefaultTableModel model = (DefaultTableModel) availableTable.getModel();
        model.setRowCount(0);
        List<VolunteerOpportunity> list = volunteerService.getAvailableOpportunities();
        for (VolunteerOpportunity opp : list) {
            model.addRow(new Object[]{
                    opp.getId(), opp.getTitle(), opp.getDate(), opp.getLocation()
            });
        }
    }

    private void loadMySignups() {
        DefaultTableModel model = (DefaultTableModel) mySignupsTable.getModel();
        model.setRowCount(0);
        List<ParticipationRecord> list = volunteerService.getParticipationForVolunteer(volunteer.getId());
        for (ParticipationRecord pr : list) {
            model.addRow(new Object[]{
                    pr.getId(), pr.getOpportunityId(), pr.getHours(), pr.isApproved()
            });
        }
    }

    public JLabel getStatsLabel() {
        return statsLabel;
    }
}