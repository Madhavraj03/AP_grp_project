package edu.univ.erp.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import edu.univ.erp.data.ConnectionFactory;
import edu.univ.erp.data.ERPDaoJdbcImpl;
import edu.univ.erp.service.AuthServiceJdbcImpl;
import edu.univ.erp.auth.JBCryptPasswordHasher;
import edu.univ.erp.domain.UserAuth;
import edu.univ.erp.ui.admin.AdminDashboardWindow;
import edu.univ.erp.ui.instructor.InstructorDashboardWindow;
import edu.univ.erp.ui.student.StudentDashboardWindow;

/**
 * LoginFrame with real database authentication.
 * Uses AuthService to verify credentials against the database.
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
                onLogin();
            }
        });
    }

    private void onLogin() {
        String u = userField.getText().trim();
        String p = new String(passField.getPassword());
        try {
            // 1. Initialize dependencies (In a real app, do this once in Main)
            ConnectionFactory.initFromFile("src/main/resources/application.properties"); 
            ERPDaoJdbcImpl dao = new ERPDaoJdbcImpl(ConnectionFactory.authDataSource(), ConnectionFactory.erpDataSource());
            AuthServiceJdbcImpl auth = new AuthServiceJdbcImpl(dao, new JBCryptPasswordHasher());
            // 2. Attempt Login
            UserAuth user = auth.login(u, p);
            
            // 3. Route based on Role
            dispose(); // Close login
            String role = user.getRole().toLowerCase();
            
            if (role.equals("admin")) {
                new AdminDashboardWindow().setVisible(true);
            } else if (role.equals("instructor")) {
                // We pass the user ID so the dashboard knows WHO is logged in
                new InstructorDashboardWindow(user.getUserId()).setVisible(true);
            } else if (role.equals("student")) {
                new StudentDashboardWindow(user.getUserId()).setVisible(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Login Failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); // For debugging
        }
    }

    public void showUI(){ SwingUtilities.invokeLater(() -> setVisible(true)); }

    public static void main(String[] args) {
        // allow running this frame directly for quick testing
        LoginFrame f = new LoginFrame();
        f.showUI();
    }
}
