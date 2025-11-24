package edu.univ.erp.ui;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.showUI();
        });
    }
}
