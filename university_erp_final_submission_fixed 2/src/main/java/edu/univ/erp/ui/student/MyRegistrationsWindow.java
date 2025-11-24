package edu.univ.erp.ui.student;

import javax.swing.*;
import java.awt.*;

public class MyRegistrationsWindow extends JFrame {
    private long studentId;
    public MyRegistrationsWindow(long studentId) {
        super("My Registrations");
        this.studentId = studentId;
        init();
    }
    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700,500); setLocationRelativeTo(null);
        add(new JLabel("List of registered sections (student id=" + studentId + ")"));
    }
}
