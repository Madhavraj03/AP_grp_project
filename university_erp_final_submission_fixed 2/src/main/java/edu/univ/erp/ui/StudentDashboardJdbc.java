package edu.univ.erp.ui;

import javax.swing.*;
import java.awt.*;
import edu.univ.erp.data.ERPDaoJdbcImpl;
import edu.univ.erp.service.StudentServiceJdbcImpl;
import java.util.List;
import edu.univ.erp.data.ERPDaoJdbcImpl.SectionInfo;

public class StudentDashboardJdbc extends JFrame {
    private ERPDaoJdbcImpl dao;
    private StudentServiceJdbcImpl studentService;
    private long studentId;
    public StudentDashboardJdbc(ERPDaoJdbcImpl dao, long studentId) {
        super("Student Dashboard"); this.dao = dao; this.studentId = studentId;
        this.studentService = new StudentServiceJdbcImpl(dao);
        initComponents();
    }
    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        add(new JLabel("Student Dashboard — catalog and actions"), BorderLayout.NORTH);
        JPanel center = new JPanel(); center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        for(var sInfo : dao.listSectionsSimple()) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            row.add(new JLabel(sInfo.courseCode + " - " + sInfo.dayTime + " cap:" + sInfo.capacity));
            JButton reg = new JButton("Register");
            reg.addActionListener(ev -> {
                try {
                    studentService.registerSection(studentId, sInfo.id);
                    JOptionPane.showMessageDialog(this, "Registered successfully");
                } catch(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
            });
            row.add(reg);
            center.add(row);
        }
        add(new JScrollPane(center), BorderLayout.CENTER);
        setSize(900,600); setLocationRelativeTo(null);
    }
}
