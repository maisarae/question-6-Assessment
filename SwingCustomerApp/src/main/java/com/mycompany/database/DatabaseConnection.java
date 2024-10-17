package com.mycompany.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Ensure the correct database name is used here (case-sensitive)
    private static final String URL = "jdbc:mysql://localhost:3306/customerdb";
    private static final String USER = "root";  // Your MySQL username
    private static final String PASSWORD = "12345";  // Your MySQL password

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();  // Print the error details
        }
        return connection;
    }
}
