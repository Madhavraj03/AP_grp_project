package edu.univ.erp.ui.student;

import javax.swing.*;
import java.awt.*;

public class TranscriptWindow extends JFrame {
    private long studentId;
    public TranscriptWindow(long studentId) {
        super("Download Transcript");
        this.studentId = studentId;
        init();
    }
    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,200); setLocationRelativeTo(null);
        JPanel p = new JPanel();
        p.add(new JLabel("Export transcript as CSV or PDF"));
        JButton csv = new JButton("Export CSV"); csv.addActionListener(e -> JOptionPane.showMessageDialog(this, "CSV exported (placeholder)"));
        JButton pdf = new JButton("Export PDF"); pdf.addActionListener(e -> JOptionPane.showMessageDialog(this, "PDF exported (placeholder)"));
        p.add(csv); p.add(pdf);
        add(p);
    }
}
