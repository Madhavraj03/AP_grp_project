package edu.univ.erp.ui.admin;

import javax.swing.*;
import java.awt.*;

public class AdminDashboardWindow extends JFrame {
    public AdminDashboardWindow() {
        super("Admin Dashboard");
        init();
    }
    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000,700); setLocationRelativeTo(null);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Admin Dashboard"));
        JButton users = new JButton("User Management"); users.addActionListener(e -> new UserManagementWindow().setVisible(true));
        JButton courses = new JButton("Course Management"); courses.addActionListener(e -> new CourseManagementWindow().setVisible(true));
        JButton sections = new JButton("Section Management"); sections.addActionListener(e -> new SectionManagementWindow().setVisible(true));
        JButton assign = new JButton("Assign Instructor"); assign.addActionListener(e -> new AssignInstructorWindow().setVisible(true));
        JButton maintenance = new JButton("Maintenance"); maintenance.addActionListener(e -> new MaintenanceWindow().setVisible(true));
        top.add(users); top.add(courses); top.add(sections); top.add(assign); top.add(maintenance);
        add(top, BorderLayout.NORTH);
        add(new JLabel("Admin Controls", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
