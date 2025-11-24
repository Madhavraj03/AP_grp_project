package edu.univ.erp.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Simple LoginFrame patched: trims inputs, prints debug to stdout, and performs demo login checks.
 * This is a safe UI-only patch to unblock login for testing. It does not modify DB.
 */
public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;

    public LoginFrame() {
        super("ERP Login");
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 260);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel center = new JPanel();
        center.setLayout(new GridLayout(4, 1));
        center.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(userLabel.getFont().deriveFont(18f));
        center.add(userLabel);

        userField = new JTextField();
        center.add(userField);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(passLabel.getFont().deriveFont(18f));
        center.add(passLabel);

        passField = new JPasswordField();
        center.add(passField);

        add(center, BorderLayout.CENTER);

        JPanel south = new JPanel();
        loginButton = new JButton("Login");
        south.add(loginButton);
        add(south, BorderLayout.SOUTH);

        // Action listener
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("DEBUG: Login button clicked");
                String u = userField.getText();
                String p = new String(passField.getPassword());
                if (u != null) u = u.trim();
                if (p != null) p = p.trim();
                System.out.println("DEBUG: username=[" + u + "], passwordLength=" + (p==null?0:p.length()));

                // Demo credentials (matches seeded demo users)
                if ("admin1".equals(u) && "adminpass".equals(p)) {
                    SwingUtilities.invokeLater(() -> {
                        AdminDashboardWindow w = new AdminDashboardWindow();
                        dispose();
                        w.setVisible(true);
                    });
                } else if ("inst1".equals(u) && "adminpass".equals(p)) {
                    SwingUtilities.invokeLater(() -> {
                        InstructorDashboardWindow w = new InstructorDashboardWindow(2L);
                        dispose();
                        w.setVisible(true);
                    });
                } else if ("stu1".equals(u) && "adminpass".equals(p)) {
                    SwingUtilities.invokeLater(() -> {
                        StudentDashboardWindow w = new StudentDashboardWindow(3L);
                        dispose();
                        w.setVisible(true);
                    });
                } else if ("stu2".equals(u) && "adminpass".equals(p)) {
                    SwingUtilities.invokeLater(() -> {
                        StudentDashboardWindow w = new StudentDashboardWindow(4L);
                        dispose();
                        w.setVisible(true);
                    });
                } else {
                    // Show friendly message
                    JOptionPane.showMessageDialog(LoginFrame.this, "Incorrect username or password.", "Login failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void showUI(){ SwingUtilities.invokeLater(() -> setVisible(true)); }

    public static void main(String[] args) {
        // allow running this frame directly for quick testing
        LoginFrame f = new LoginFrame();
        f.showUI();
    }
}
