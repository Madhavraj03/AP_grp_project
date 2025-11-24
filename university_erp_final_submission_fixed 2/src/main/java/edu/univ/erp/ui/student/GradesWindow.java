package edu.univ.erp.ui.student;

import javax.swing.*;
import java.awt.*;

public class GradesWindow extends JFrame {
    private long studentId;
    public GradesWindow(long studentId) {
        super("Grades");
        this.studentId = studentId;
        init();
    }
    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700,500); setLocationRelativeTo(null);
        add(new JLabel("Grades and assessment components for student " + studentId));
    }
}
