package edu.univ.erp.ui.admin;

import javax.swing.*;
import java.awt.*;

public class CourseManagementWindow extends JFrame {
    public CourseManagementWindow() { super("Course Management"); init(); }
    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800,600); setLocationRelativeTo(null);
        add(new JLabel("Add / Edit courses"));
    }
}
