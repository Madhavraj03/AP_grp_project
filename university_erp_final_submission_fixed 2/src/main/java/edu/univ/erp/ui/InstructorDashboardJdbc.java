package edu.univ.erp.ui;

import javax.swing.*;
import java.awt.*;
import edu.univ.erp.data.ERPDaoJdbcImpl;
import edu.univ.erp.service.InstructorServiceJdbcImpl;

public class InstructorDashboardJdbc extends JFrame {
    private ERPDaoJdbcImpl dao; private long instructorId;
    private InstructorServiceJdbcImpl instrService;
    public InstructorDashboardJdbc(ERPDaoJdbcImpl dao, long instructorId) {
        super("Instructor Dashboard"); this.dao = dao; this.instructorId = instructorId;
        this.instrService = new InstructorServiceJdbcImpl(dao);
        initComponents();
    }
    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        add(new JLabel("Instructor Dashboard — My sections and grade entry"), BorderLayout.NORTH);
        // minimal implementation: list sections taught by instructor, allow compute final for first enrollment found
        var sections = dao.getSectionsByInstructor(instructorId);
        JPanel center = new JPanel(); center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        for(var s: sections) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            row.add(new JLabel("Section " + s.getId() + " - " + s.getDayTime()));
            JButton stats = new JButton("Stats");
            stats.addActionListener(ev -> {
                var m = instrService.simpleStats(s.getId());
                JOptionPane.showMessageDialog(this, "Avg: " + m.get("avg") + ", sd: " + m.get("sd"));
            });
            row.add(stats);
            center.add(row);
        }
        add(new JScrollPane(center), BorderLayout.CENTER);
        setSize(900,600); setLocationRelativeTo(null);
    }
}
