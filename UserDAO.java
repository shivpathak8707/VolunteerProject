package com.volunteerapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO implements ICRUDRepository<User, Integer> {

    @Override
    public User save(User user) throws AppException {
        String sql = "INSERT INTO users (name, email, role) VALUES (?, ?, ?)";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getRole().name());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    // int generatedId = rs.getInt(1);
                    // For simplicity we are not updating user.id here
                }
            }
            return user;

        } catch (SQLException e) {
            throw new AppException("Error saving user", e);
        }
    }

    @Override
    public Optional<User> findById(Integer id) throws AppException {
        String sql = "SELECT id, name, email, role FROM users WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String roleStr = rs.getString("role");
                    UserRole role = UserRole.valueOf(roleStr);
                    int uid = rs.getInt("id");
                    String name = rs.getString("name");
                    String email = rs.getString("email");

                    User user;
                    switch (role) {
                        case ADMIN:
                            user = new Admin(uid, name, email);
                            break;
                        case ORGANIZATION:
                            user = new Organization(uid, name, email, name + " Org");
                            break;
                        case VOLUNTEER:
                            user = new Volunteer(uid, name, email);
                            break;
                        default:
                            user = null;
                    }
                    return Optional.ofNullable(user);
                }
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new AppException("Error finding user by id", e);
        }
    }

    @Override
    public List<User> findAll() throws AppException {
        String sql = "SELECT id, name, email, role FROM users";
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String roleStr = rs.getString("role");
                UserRole role = UserRole.valueOf(roleStr);
                int uid = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");

                User user;
                switch (role) {
                    case ADMIN:
                        user = new Admin(uid, name, email);
                        break;
                    case ORGANIZATION:
                        user = new Organization(uid, name, email, name + " Org");
                        break;
                    case VOLUNTEER:
                        user = new Volunteer(uid, name, email);
                        break;
                    default:
                        user = null;
                }
                if (user != null) users.add(user);
            }
        } catch (SQLException e) {
            throw new AppException("Error loading users", e);
        }
        return users;
    }

    @Override
    public void deleteById(Integer id) throws AppException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AppException("Error deleting user", e);
        }
    }
}
