package edu.univ.erp.ui.student;

import javax.swing.*;
import java.awt.*;

public class CatalogWindow extends JFrame {
    private long studentId;
    public CatalogWindow(long studentId) {
        super("Course Catalog");
        this.studentId = studentId;
        init();
    }
    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800,600); setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(new JLabel("Catalog: code, title, credits, capacity, instructor"), BorderLayout.NORTH);
        // TODO: connect to CatalogApi to populate a JTable
        JTextArea ta = new JTextArea("Sample catalog will appear here.");
        add(new JScrollPane(ta), BorderLayout.CENTER);
    }
}
