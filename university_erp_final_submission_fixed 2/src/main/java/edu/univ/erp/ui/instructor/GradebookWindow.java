package edu.univ.erp.ui.instructor;

import javax.swing.*;
import java.awt.*;

public class GradebookWindow extends JFrame {
    private long instructorId;
    public GradebookWindow(long instructorId) {
        super("Gradebook");
        this.instructorId = instructorId;
        init();
    }
    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900,600); setLocationRelativeTo(null);
        add(new JLabel("Grade entry UI: assessments, save, compute final grades"));
    }
}
