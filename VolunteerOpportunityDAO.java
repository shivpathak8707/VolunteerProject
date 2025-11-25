package com.volunteerapp;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VolunteerOpportunityDAO implements ICRUDRepository<VolunteerOpportunity, Integer> {

    @Override
    public VolunteerOpportunity save(VolunteerOpportunity opp) throws AppException {
        String sql = "INSERT INTO opportunities (title, description, date, location, status, organization_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, opp.getTitle());
            ps.setString(2, opp.getDescription());
            ps.setDate(3, Date.valueOf(opp.getDate()));
            ps.setString(4, opp.getLocation());
            ps.setString(5, opp.getStatus().name());
            ps.setInt(6, opp.getOrganizationId());

            ps.executeUpdate();
            return opp;

        } catch (SQLException e) {
            throw new AppException("Error saving opportunity", e);
        }
    }

    @Override
    public Optional<VolunteerOpportunity> findById(Integer id) throws AppException {
        String sql = "SELECT id, title, description, date, location, status, organization_id " +
                     "FROM opportunities WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    VolunteerOpportunity opp = mapRow(rs);
                    return Optional.of(opp);
                }
            }
        } catch (SQLException e) {
            throw new AppException("Error finding opportunity", e);
        }
        return Optional.empty();
    }

    @Override
    public List<VolunteerOpportunity> findAll() throws AppException {
        String sql = "SELECT id, title, description, date, location, status, organization_id FROM opportunities";
        List<VolunteerOpportunity> list = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new AppException("Error loading opportunities", e);
        }
        return list;
    }

    @Override
    public void deleteById(Integer id) throws AppException {
        String sql = "DELETE FROM opportunities WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new AppException("Error deleting opportunity", e);
        }
    }

    private VolunteerOpportunity mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String desc = rs.getString("description");
        LocalDate date = rs.getDate("date").toLocalDate();
        String location = rs.getString("location");
        OpportunityStatus status = OpportunityStatus.valueOf(rs.getString("status"));
        int orgId = rs.getInt("organization_id");
        return new VolunteerOpportunity(id, title, desc, date, location, status, orgId);
    }
}