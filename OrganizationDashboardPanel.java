package com.volunteerapp;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OrganizationDashboardPanel extends JPanel {

    private final Organization organization;
    private final OrganizationService orgService;
    private final MessagingService messagingService;
    private final List<User> allUsers;

    private JTable opportunitiesTable;
    private JTextArea messagesArea;

    public OrganizationDashboardPanel(Organization organization,
                                      OrganizationService orgService,
                                      MessagingService messagingService,
                                      List<User> allUsers) {
        this.organization = organization;
        this.orgService = orgService;
        this.messagingService = messagingService;
        this.allUsers = allUsers;
        initUI();
        loadOpportunities();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(220, 20, 60));

        JTabbedPane tabs = new JTabbedPane();

        JPanel postPanel = createPostOpportunityPanel();
        JPanel interactionPanel = createInteractionPanel();
        JPanel participationPanel = createParticipationPanel();

        tabs.addTab("Post Opportunities", postPanel);
        tabs.addTab("Volunteer Interaction", interactionPanel);
        tabs.addTab("Participation Tracking", participationPanel);

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createPostOpportunityPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBackground(new Color(255, 215, 0));

        JTextField titleField = new JTextField();
        JTextField descField = new JTextField();
        JTextField dateField = new JTextField("2025-12-31");
        JTextField locationField = new JTextField();

        JButton postButton = new JButton("Post Opportunity");
        postButton.addActionListener(e -> {
            try {
                String title = titleField.getText().trim();
                String desc = descField.getText().trim();
                String dateStr = dateField.getText().trim();
                String location = locationField.getText().trim();

                if (title.isEmpty() || desc.isEmpty() || location.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields are required");
                    return;
                }

                LocalDate date = LocalDate.parse(dateStr);
                orgService.postOpportunity(organization.getId(), title, desc, date, location);
                JOptionPane.showMessageDialog(this, "Opportunity posted (Pending Approval)");
                loadOpportunities();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Description:"));
        panel.add(descField);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Location:"));
        panel.add(locationField);
        panel.add(new JLabel());
        panel.add(postButton);

        return panel;
    }

    private JPanel createInteractionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 215, 0));

        messagesArea = new JTextArea();
        messagesArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(messagesArea);

        JPanel sendPanel = new JPanel();
        sendPanel.setBackground(new Color(255, 230, 128));

        JComboBox<User> volunteerCombo = new JComboBox<>();
        for (User u : allUsers) {
            if (u.getRole() == UserRole.VOLUNTEER) {
                volunteerCombo.addItem(u);
            }
        }
        JTextField messageField = new JTextField(20);
        JButton sendBtn = new JButton("Send");

        sendBtn.addActionListener(e -> {
            User to = (User) volunteerCombo.getSelectedItem();
            if (to == null) return;
            try {
                messagingService.sendMessage(organization, to, messageField.getText());
                JOptionPane.showMessageDialog(this, "Message sent");
                messageField.setText("");
            } catch (AppException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        sendPanel.add(new JLabel("Volunteer:"));
        sendPanel.add(volunteerCombo);
        sendPanel.add(messageField);
        sendPanel.add(sendBtn);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(sendPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createParticipationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 215, 0));

        String[] columns = {"Participation ID", "Volunteer ID", "Opportunity ID", "Hours", "Approved"};
        JTable table = new JTable(new DefaultTableModel(columns, 0));
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void loadOpportunities() {
        if (opportunitiesTable == null) {
            String[] columns = {"ID", "Title", "Date", "Location", "Status"};
            opportunitiesTable = new JTable(new DefaultTableModel(columns, 0));
            add(new JScrollPane(opportunitiesTable), BorderLayout.NORTH);
        }
        DefaultTableModel model = (DefaultTableModel) opportunitiesTable.getModel();
        model.setRowCount(0);
        List<VolunteerOpportunity> list = orgService.getOpportunitiesByOrg(organization.getId());
        for (VolunteerOpportunity opp : list) {
            model.addRow(new Object[]{
                    opp.getId(), opp.getTitle(), opp.getDate(), opp.getLocation(), opp.getStatus()
            });
        }
    }
}

