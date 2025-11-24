package edu.univ.erp.ui.instructor;

import javax.swing.*;
import java.awt.*;

public class StatsWindow extends JFrame {
    private long instructorId;
    public StatsWindow(long instructorId) {
        super("Statistics");
        this.instructorId = instructorId;
        init();
    }
    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700,500); setLocationRelativeTo(null);
        add(new JLabel("Simple class statistics (average, distribution)"));
    }
}
