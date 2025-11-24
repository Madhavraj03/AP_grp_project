package edu.univ.erp.ui.instructor;

import javax.swing.*;
import java.awt.*;

public class MySectionsWindow extends JFrame {
    private long instructorId;
    public MySectionsWindow(long instructorId) {
        super("My Sections");
        this.instructorId = instructorId;
        init();
    }
    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800,600); setLocationRelativeTo(null);
        add(new JLabel("Sections assigned to instructor " + instructorId));
    }
}
