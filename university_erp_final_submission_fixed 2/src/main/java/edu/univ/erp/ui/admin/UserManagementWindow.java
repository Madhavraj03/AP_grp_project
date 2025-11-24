package edu.univ.erp.ui.admin;

import javax.swing.*;
import java.awt.*;
import edu.univ.erp.data.ConnectionFactory;
import edu.univ.erp.data.ERPDaoJdbcImpl;
import edu.univ.erp.service.AdminServiceJdbcImpl;

public class UserManagementWindow extends JFrame {
    private ERPDaoJdbcImpl dao;
    private AdminServiceJdbcImpl adminService;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private JTextField extraInfoField;

    public UserManagementWindow() {
        super("User Management");
        // Initialize services
        this.dao = new ERPDaoJdbcImpl(ConnectionFactory.authDataSource(), ConnectionFactory.erpDataSource());
        this.adminService = new AdminServiceJdbcImpl(dao);
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Center Panel with form
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(5, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Username
        centerPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        centerPanel.add(usernameField);

        // Password
        centerPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        centerPanel.add(passwordField);

        // Role
        centerPanel.add(new JLabel("Role:"));
        roleBox = new JComboBox<>(new String[]{"student", "instructor", "admin"});
        centerPanel.add(roleBox);

        // Extra Info (RollNo for student, Department for instructor)
        centerPanel.add(new JLabel("Extra Info (RollNo/Dept):"));
        extraInfoField = new JTextField();
        centerPanel.add(extraInfoField);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel with Create Button
        JPanel bottomPanel = new JPanel();
        JButton createBtn = new JButton("Create User");
        createBtn.addActionListener(e -> createUser());
        bottomPanel.add(createBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void createUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleBox.getSelectedItem();
        String extraInfo = extraInfoField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and Password are required.");
            return;
        }

        try {
            adminService.createUser(username, password, role, extraInfo);
            JOptionPane.showMessageDialog(this, "User created successfully!");
            // Clear fields
            usernameField.setText("");
            passwordField.setText("");
            extraInfoField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to create user: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
