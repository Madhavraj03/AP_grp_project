package edu.univ.erp.ui.admin;

import javax.swing.*;
import java.awt.*;

public class UserManagementWindow extends JFrame {
    public UserManagementWindow() { super("User Management"); init(); }
    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800,600); setLocationRelativeTo(null);
        add(new JLabel("Create / edit users (students/instructors/admins)"));
    }
}
