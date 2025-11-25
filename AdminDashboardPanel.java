package com.volunteerapp;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminDashboardPanel extends JPanel {

    private final AdminService adminService;
    private JTable userTable;
    private JTable opportunityTable;

    public AdminDashboardPanel(AdminService adminService) {
        this.adminService = adminService;
        initUI();
        loadUsers();
        loadOpportunities();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(220, 20, 60)); // red-ish

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel userMgmtPanel = createUserManagementPanel();
        JPanel oppMgmtPanel = createOpportunityManagementPanel();
        JPanel settingsPanel = createSettingsPanel();

        tabbedPane.addTab("User Management", userMgmtPanel);
        tabbedPane.addTab("Listings Management", oppMgmtPanel);
        tabbedPane.addTab("System Settings", settingsPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 215, 0)); // yellow

        String[] columns = {"ID", "Name", "Email", "Role"};
        userTable = new JTable(new DefaultTableModel(columns, 0));
        JScrollPane scrollPane = new JScrollPane(userTable);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(255, 230, 128));
        JTextField nameField = new JTextField(10);
        JTextField emailField = new JTextField(10);
        JComboBox<UserRole> roleBox = new JComboBox<>(UserRole.values());
        JButton addButton = new JButton("Add User");

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            UserRole role = (UserRole) roleBox.getSelectedItem();

            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Email required");
                return;
            }

            int newId = adminService.getAllUsers().size() + 1;
            User user;
            switch (role) {
                case ADMIN:
                    user = new Admin(newId, name, email);
                    break;
                case ORGANIZATION:
                    user = new Organization(newId, name, email, name + " Org");
                    break;
                case VOLUNTEER:
                    user = new Volunteer(newId, name, email);
                    break;
                default:
                    user = new Admin(newId, name, email);
            }
            adminService.addUser(user);
            loadUsers();
        });

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleBox);
        formPanel.add(addButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createOpportunityManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 215, 0));

        String[] columns = {"ID", "Title", "Date", "Location", "Status"};
        opportunityTable = new JTable(new DefaultTableModel(columns, 0));
        JScrollPane scrollPane = new JScrollPane(opportunityTable);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 230, 128));
        JButton approveBtn = new JButton("Approve");
        JButton rejectBtn = new JButton("Reject");

        approveBtn.addActionListener(e -> changeStatus(OpportunityStatus.APPROVED));
        rejectBtn.addActionListener(e -> changeStatus(OpportunityStatus.REJECTED));

        buttonPanel.add(approveBtn);
        buttonPanel.add(rejectBtn);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(new Color(255, 230, 128));

        JLabel label1 = new JLabel("Max Volunteers per Opportunity:");
        JTextField field1 = new JTextField("50");

        JLabel label2 = new JLabel("Default Approval Timeout (days):");
        JTextField field2 = new JTextField("3");

        JButton saveBtn = new JButton("Save Settings");
        saveBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Settings saved (mock)")
        );

        panel.add(label1);
        panel.add(field1);
        panel.add(label2);
        panel.add(field2);
        panel.add(new JLabel());
        panel.add(saveBtn);

        return panel;
    }

    private void loadUsers() {
        DefaultTableModel model = (DefaultTableModel) userTable.getModel();
        model.setRowCount(0);
        for (User u : adminService.getAllUsers()) {
            model.addRow(new Object[]{
                    u.getId(), u.getName(), u.getEmail(), u.getRole()
            });
        }
    }

    private void loadOpportunities() {
        DefaultTableModel model = (DefaultTableModel) opportunityTable.getModel();
        model.setRowCount(0);
        List<VolunteerOpportunity> list = adminService.getAllOpportunities();
        for (VolunteerOpportunity opp : list) {
            model.addRow(new Object[]{
                    opp.getId(), opp.getTitle(), opp.getDate(),
                    opp.getLocation(), opp.getStatus()
            });
        }
    }

    private void changeStatus(OpportunityStatus status) {
        int row = opportunityTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an opportunity");
            return;
        }
        int oppId = (int) opportunityTable.getValueAt(row, 0);
        if (status == OpportunityStatus.APPROVED) {
            adminService.approveOpportunity(oppId);
        } else {
            adminService.rejectOpportunity(oppId);
        }
        loadOpportunities();
    }
}
