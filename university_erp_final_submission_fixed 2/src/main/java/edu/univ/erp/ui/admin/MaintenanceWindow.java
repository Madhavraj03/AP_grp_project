package edu.univ.erp.ui.admin;

import javax.swing.*;
import java.awt.*;

public class MaintenanceWindow extends JFrame {
    public MaintenanceWindow() { super("Maintenance Mode"); init(); }
    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,200); setLocationRelativeTo(null);
        JPanel p = new JPanel();
        JLabel lbl = new JLabel("Maintenance Mode: OFF");
        JButton toggle = new JButton("Toggle Maintenance");
        toggle.addActionListener(e -> JOptionPane.showMessageDialog(this, "Toggled (placeholder)"));
        p.add(lbl); p.add(toggle);
        add(p);
    }
}
