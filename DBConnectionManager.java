package com.volunteerapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {

    // Change URL, USER, PASSWORD according to your DB
    private static final String URL = "jdbc:mysql://localhost:3306/volunteer_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static Connection getConnection() throws AppException {
        try {
            // Ensure driver is in classpath: com.mysql.cj.jdbc.Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new AppException("JDBC Driver not found", e);
        } catch (SQLException e) {
            throw new AppException("Error connecting to database", e);
        }
    }
}