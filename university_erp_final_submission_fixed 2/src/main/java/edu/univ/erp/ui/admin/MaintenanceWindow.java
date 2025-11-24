package edu.univ.erp.ui.admin;

import javax.swing.*;
import java.awt.*;
import edu.univ.erp.data.ConnectionFactory;
import edu.univ.erp.data.ERPDaoJdbcImpl;

public class MaintenanceWindow extends JFrame {
    private ERPDaoJdbcImpl dao;
    
    public MaintenanceWindow() { 
        super("Maintenance Mode");
        this.dao = new ERPDaoJdbcImpl(ConnectionFactory.authDataSource(), ConnectionFactory.erpDataSource());
        init();
    }
    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,200); setLocationRelativeTo(null);
        JPanel p = new JPanel();
        JLabel lbl = new JLabel("Maintenance Mode: OFF");
        JButton toggle = new JButton("Toggle Maintenance");
        toggle.addActionListener(e -> {
            boolean current = dao.isMaintenanceOn();
            dao.setMaintenance(!current);
            JOptionPane.showMessageDialog(this, "Maintenance is now: " + (!current));
        });
        p.add(lbl); p.add(toggle);
        add(p);
    }
}
