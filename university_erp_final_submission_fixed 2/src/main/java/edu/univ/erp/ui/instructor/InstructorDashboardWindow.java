package edu.univ.erp.ui.instructor;

import javax.swing.*;
import java.awt.*;

public class InstructorDashboardWindow extends JFrame {
    private long instructorId;
    public InstructorDashboardWindow(long instructorId) {
        super("Instructor Dashboard");
        this.instructorId = instructorId;
        init();
    }
    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000,700); setLocationRelativeTo(null);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Instructor Dashboard - ID: " + instructorId));
        JButton mySections = new JButton("My Sections"); mySections.addActionListener(e -> new MySectionsWindow(instructorId).setVisible(true));
        JButton gradebook = new JButton("Gradebook"); gradebook.addActionListener(e -> new GradebookWindow(instructorId).setVisible(true));
        JButton stats = new JButton("Stats"); stats.addActionListener(e -> new StatsWindow(instructorId).setVisible(true));
        top.add(mySections); top.add(gradebook); top.add(stats);
        add(top, BorderLayout.NORTH);
        add(new JLabel("Welcome Instructor", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
