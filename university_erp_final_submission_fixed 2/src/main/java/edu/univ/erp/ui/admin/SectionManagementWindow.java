package edu.univ.erp.ui.admin;

import javax.swing.*;
import java.awt.*;

public class SectionManagementWindow extends JFrame {
    public SectionManagementWindow() { super("Section Management"); init(); }
    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800,600); setLocationRelativeTo(null);
        add(new JLabel("Create / edit sections; assign instructor; capacity; semester"));
    }
}
