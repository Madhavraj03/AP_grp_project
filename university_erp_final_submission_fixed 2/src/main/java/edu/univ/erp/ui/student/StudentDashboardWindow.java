package edu.univ.erp.ui.student;

import javax.swing.*;
import java.awt.*;
import edu.univ.erp.ui.admin.AdminDashboardWindow;

public class StudentDashboardWindow extends JFrame {
    private long studentId;
    public StudentDashboardWindow(long studentId) {
        super("Student Dashboard");
        this.studentId = studentId;
        init();
    }
    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000,700); setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Student Dashboard - ID: " + studentId));
        JButton catalog = new JButton("Catalog"); catalog.addActionListener(e -> new CatalogWindow(studentId).setVisible(true));
        JButton regs = new JButton("My Registrations"); regs.addActionListener(e -> new MyRegistrationsWindow(studentId).setVisible(true));
        JButton tt = new JButton("Timetable"); tt.addActionListener(e -> new TimetableWindow(studentId).setVisible(true));
        JButton grades = new JButton("Grades"); grades.addActionListener(e -> new GradesWindow(studentId).setVisible(true));
        JButton transcript = new JButton("Transcript"); transcript.addActionListener(e -> new TranscriptWindow(studentId).setVisible(true));
        top.add(catalog); top.add(regs); top.add(tt); top.add(grades); top.add(transcript);
        add(top, BorderLayout.NORTH);
        add(new JLabel("Welcome to the Student Dashboard", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
