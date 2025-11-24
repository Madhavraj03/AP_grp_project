package edu.univ.erp.ui.student;

import javax.swing.*;
import java.awt.*;

public class TimetableWindow extends JFrame {
    private long studentId;
    public TimetableWindow(long studentId) {
        super("Timetable");
        this.studentId = studentId;
        init();
    }
    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700,500); setLocationRelativeTo(null);
        add(new JLabel("Timetable view for student " + studentId));
    }
}
