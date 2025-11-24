package edu.univ.erp.ui;

import javax.swing.*;
import java.awt.*;
import edu.univ.erp.data.ERPDaoJdbcImpl;

public class AdminDashboardJdbc extends JFrame {
    private ERPDaoJdbcImpl dao;
    public AdminDashboardJdbc(ERPDaoJdbcImpl dao) { super("Admin Dashboard"); this.dao=dao; init(); }
    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Admin Dashboard")); 
        JButton toggle = new JButton("Toggle Maintenance");
        toggle.addActionListener(ev -> {
            boolean on = dao.isMaintenanceOn();
            dao.setMaintenance(!on);
            JOptionPane.showMessageDialog(this, "Maintenance set to " + (!on));
        });
        top.add(toggle);
        add(top, BorderLayout.NORTH);
        setSize(900,600); setLocationRelativeTo(null);
    }
}
