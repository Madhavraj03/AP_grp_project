package edu.univ.erp.ui.instructor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import edu.univ.erp.data.ConnectionFactory;
import edu.univ.erp.data.ERPDaoJdbcImpl;
import edu.univ.erp.service.InstructorServiceJdbcImpl;
import edu.univ.erp.domain.Grade;

public class GradebookWindow extends JFrame {
    private long instructorId;
    private ERPDaoJdbcImpl dao;
    private InstructorServiceJdbcImpl service;
    private JComboBox<String> sectionBox;
    private JTable table;
    private DefaultTableModel model;
    private java.util.List<edu.univ.erp.domain.Section> mySections;

    public GradebookWindow(long instructorId) {
        super("Gradebook");
        this.instructorId = instructorId;
        this.dao = new ERPDaoJdbcImpl(ConnectionFactory.authDataSource(), ConnectionFactory.erpDataSource());
        this.service = new InstructorServiceJdbcImpl(dao);
        init();
    }

    private void init() {
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top: Section Selector
        JPanel top = new JPanel();
        sectionBox = new JComboBox<>();
        JButton loadBtn = new JButton("Load Students");
        
        mySections = dao.getSectionsByInstructor(instructorId);
        for(var s : mySections) {
            sectionBox.addItem("Section " + s.getId() + " (" + s.getDayTime() + ")");
        }
        
        loadBtn.addActionListener(e -> loadGrades());
        top.add(sectionBox);
        top.add(loadBtn);
        add(top, BorderLayout.NORTH);

        // Center: Grade Table
        // We will mock columns: EnrollmentID, Component, Score
        // Note: Real world would be pivot table (Student | Quiz | Mid | Final), 
        // but simple "Row per Grade" is easier for this assignment deadline.
        model = new DefaultTableModel(new String[]{"Enrollment ID", "Component (quiz/midterm/endsem)", "Score"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom: Save
        JButton saveBtn = new JButton("Save Grades");
        saveBtn.addActionListener(e -> saveGrades());
        add(saveBtn, BorderLayout.SOUTH);
    }

    private void loadGrades() {
        int idx = sectionBox.getSelectedIndex();
        if(idx == -1) return;
        long sectionId = mySections.get(idx).getId();
        
        model.setRowCount(0);
        var grades = service.getGradesForSection(sectionId);
        for(Grade g : grades) {
            model.addRow(new Object[]{g.getEnrollmentId(), g.getComponent(), g.getScore()});
        }
        if(grades.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No grades found (or no students enrolled).");
        }
    }

    private void saveGrades() {
        try {
            for(int i=0; i<model.getRowCount(); i++) {
                long eid = Long.parseLong(model.getValueAt(i, 0).toString());
                String comp = (String) model.getValueAt(i, 1);
                double score = Double.parseDouble(model.getValueAt(i, 2).toString());
                
                service.saveGrade(new Grade(0, eid, comp, score));
                // Auto compute final
                service.computeFinalForEnrollment(eid);
            }
            JOptionPane.showMessageDialog(this, "Saved & Computed!");
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
