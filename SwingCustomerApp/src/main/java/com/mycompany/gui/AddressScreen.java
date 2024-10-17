package com.mycompany.gui;

import com.mycompany.database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddressScreen extends JFrame {
    private JTextField address1Field, address2Field, address3Field, cityField, postalCodeField;
    private int customerId;

    public AddressScreen(int customerId) {
        this.customerId = customerId;

        setTitle("Customer Address");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        // Fields
        add(new JLabel("Address 1:"));
        address1Field = new JTextField(80);
        add(address1Field);

        add(new JLabel("Address 2:"));
        address2Field = new JTextField(80);
        add(address2Field);

        add(new JLabel("Address 3:"));
        address3Field = new JTextField(80);
        add(address3Field);

        add(new JLabel("City:"));
        cityField = new JTextField(50);
        add(cityField);

        add(new JLabel("Postal Code:"));
        postalCodeField = new JTextField(5);
        add(postalCodeField);

        loadCustomerAddress();  // Load existing address data

        // Buttons
        JButton modifyButton = new JButton("Modify");
        modifyButton.addActionListener(e -> modifyAddress());
        add(modifyButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteCustomer());
        add(deleteButton);

        setVisible(true);
    }

    // Load the existing address from the database
    private void loadCustomerAddress() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Customer WHERE customer_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                address1Field.setText(rs.getString("address_1"));
                address2Field.setText(rs.getString("address_2"));
                address3Field.setText(rs.getString("address_3"));
                cityField.setText(rs.getString("city"));
                postalCodeField.setText(rs.getString("postal_code"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Modify the customer address
    private void modifyAddress() {
        if (!postalCodeField.getText().matches("\\d{5}")) {
            JOptionPane.showMessageDialog(this, "Postal code must be exactly 5 digits!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE Customer SET address_1 = ?, address_2 = ?, address_3 = ?, city = ?, postal_code = ? WHERE customer_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, address1Field.getText());
            stmt.setString(2, address2Field.getText());
            stmt.setString(3, address3Field.getText());
            stmt.setString(4, cityField.getText());
            stmt.setString(5, postalCodeField.getText());
            stmt.setInt(6, customerId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Address updated successfully!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteCustomer() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM Customer WHERE customer_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, customerId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Customer deleted successfully!");
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
