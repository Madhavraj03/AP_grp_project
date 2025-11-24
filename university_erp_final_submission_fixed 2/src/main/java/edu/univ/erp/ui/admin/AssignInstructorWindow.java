package edu.univ.erp.ui.admin;

import javax.swing.*;
import java.awt.*;

public class AssignInstructorWindow extends JFrame {
    public AssignInstructorWindow() { super("Assign Instructor"); init(); }
    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600,400); setLocationRelativeTo(null);
        add(new JLabel("Assign an instructor to a section"));
    }
}
