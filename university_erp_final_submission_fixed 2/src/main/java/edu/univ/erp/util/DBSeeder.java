package edu.univ.erp.util;

import edu.univ.erp.data.ConnectionFactory;
import edu.univ.erp.auth.JBCryptPasswordHasher;
import java.nio.file.*;
import java.sql.*;
import java.util.stream.*;
import java.io.*;
import java.util.Properties;

public class DBSeeder {
    public static void seedIfNeeded(String propsPath, String sqlDir) throws Exception {
        // initialize connections
        Properties p = new Properties();
        try (InputStream in = new FileInputStream(propsPath)) { p.load(in); }
        ConnectionFactory.init(p);

        // Run schema SQL
        runSqlFile(ConnectionFactory.authDataSource().getConnection(), Paths.get(sqlDir, "auth_schema.sql"));
        runSqlFile(ConnectionFactory.erpDataSource().getConnection(), Paths.get(sqlDir, "erp_schema.sql"));

        // Insert seed ERP data
        runSqlFile(ConnectionFactory.erpDataSource().getConnection(), Paths.get(sqlDir, "seed_erp.sql"));

        // Insert users with bcrypt using jBCrypt
        // If users already exist, skip
        try (Connection c = ConnectionFactory.authDataSource().getConnection()) {
            String check = "SELECT COUNT(*) FROM users_auth";
            try (PreparedStatement ps = c.prepareStatement(check); ResultSet rs = ps.executeQuery()) {
                if(rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Auth DB already seeded");
                } else {
                    String insert = "INSERT INTO users_auth (username, role, password_hash, status) VALUES (?, ?, ?, 'active')";
                    JBCryptPasswordHasher hasher = new JBCryptPasswordHasher();
                    try (PreparedStatement ps = c.prepareStatement(insert)) {
                        ps.setString(1, "admin1"); ps.setString(2, "admin"); ps.setString(3, hasher.hash("adminpass")); ps.executeUpdate();
                        ps.setString(1, "inst1"); ps.setString(2, "instructor"); ps.setString(3, hasher.hash("instpass")); ps.executeUpdate();
                        ps.setString(1, "stu1"); ps.setString(2, "student"); ps.setString(3, hasher.hash("stupass")); ps.executeUpdate();
                        ps.setString(1, "stu2"); ps.setString(2, "student"); ps.setString(3, hasher.hash("stupass2")); ps.executeUpdate();
                    }
                }
            }
        }
    }

    private static void runSqlFile(Connection conn, java.nio.file.Path path) throws Exception {
        if(!Files.exists(path)) return;
        String sql = new String(Files.readAllBytes(path));
        // split by ; for simple execution
        String[] parts = sql.split(";\s*\n");
        try (Statement st = conn.createStatement()) {
            for(String part: parts) {
                String t = part.trim();
                if(t.length()==0) continue;
                st.execute(t);
            }
        }
    }
}
