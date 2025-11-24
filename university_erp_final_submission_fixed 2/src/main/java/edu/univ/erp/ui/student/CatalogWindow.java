package edu.univ.erp.ui.student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import edu.univ.erp.data.ConnectionFactory;
import edu.univ.erp.data.ERPDaoJdbcImpl;
import edu.univ.erp.service.StudentServiceJdbcImpl;

public class CatalogWindow extends JFrame {
    private long studentId;
    private ERPDaoJdbcImpl dao;
    private StudentServiceJdbcImpl service;
    private JTable table;
    private DefaultTableModel model;

    public CatalogWindow(long studentId) {
        super("Course Catalog");
        this.studentId = studentId;
        // Re-init helpers (simplification for this project)
        this.dao = new ERPDaoJdbcImpl(ConnectionFactory.authDataSource(), ConnectionFactory.erpDataSource());
        this.service = new StudentServiceJdbcImpl(dao);
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Setup Table
        String[] columns = {"ID", "Course", "Day/Time", "Capacity"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 2. Load Data
        refreshData();

        // 3. Register Button
        JButton regBtn = new JButton("Register Selected");
        regBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a section first.");
                return;
            }
            long sectionId = (Long) model.getValueAt(row, 0);
            try {
                service.registerSection(studentId, sectionId);
                JOptionPane.showMessageDialog(this, "Success! Registered.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed: " + ex.getMessage());
            }
        });
        add(regBtn, BorderLayout.SOUTH);
    }

    private void refreshData() {
        model.setRowCount(0);
        // Using the dao.listSectionsSimple() method you already have
        List<ERPDaoJdbcImpl.SectionRow> rows = dao.listSectionsSimple();
        for (var r : rows) {
            model.addRow(new Object[]{r.id, r.courseCode, r.dayTime, r.capacity});
        }
    }
}
