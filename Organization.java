package com.volunteerapp;

public class Organization extends User {

    private String organizationName;

    public Organization(int id, String name, String email, String organizationName) {
        super(id, name, email);
        this.organizationName = organizationName;
    }

    @Override
    public UserRole getRole() {
        return UserRole.ORGANIZATION;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    @Override
    public String toString() {
        return organizationName + " (" + name + ")";
    }
}
