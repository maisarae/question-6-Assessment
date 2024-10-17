package com.mycompany.gui;

import com.mycompany.database.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CustomerListScreen extends JFrame {
    private JTable customerTable;
    private DefaultTableModel tableModel;

    public CustomerListScreen() {
        setTitle("Customer List");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize table model
        tableModel = new DefaultTableModel();
        customerTable = new JTable(tableModel);

        // Add columns to the table model
        tableModel.addColumn("Customer ID");
        tableModel.addColumn("Short Name");
        tableModel.addColumn("Full Name");
        tableModel.addColumn("City");
        tableModel.addColumn("Postal Code");

        // Load customer data from the database
        loadCustomers();

        // Add the table inside a scroll pane
        JScrollPane scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);

        // View button to open AddressScreen
        JButton viewButton = new JButton("View Address");
        viewButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();  // Get selected row

            if (selectedRow != -1) {  // Ensure a row is selected
                // Retrieve customer ID from the selected row
                int customerId = (int) tableModel.getValueAt(selectedRow, 0);
                System.out.println("Selected Customer ID: " + customerId);  // Debugging log

                // Open the AddressScreen for the selected customer
                new AddressScreen(customerId);  // <-- Ensure the AddressScreen is called here
            } else {
                // Show a message if no customer is selected
                JOptionPane.showMessageDialog(this, "Please select a customer!");
            }
        });

        // Add the View button at the bottom of the frame
        add(viewButton, BorderLayout.SOUTH);

        // Make the frame visible
        setVisible(true);
    }

    // Load customers from the database into the table
    private void loadCustomers() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // SQL query to get customer data
            String query = "SELECT customer_id, short_name, full_name, city, postal_code FROM Customer";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Populate the table model with customer data
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("customer_id"),
                        rs.getString("short_name"),
                        rs.getString("full_name"),
                        rs.getString("city"),
                        rs.getString("postal_code")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();  // Ensure we log any exceptions for debugging
        }
    }

    public static void main(String[] args) {
        // Launch the CustomerListScreen
        new CustomerListScreen();
    }
}
