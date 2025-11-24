package edu.univ.erp.service;

import edu.univ.erp.data.ERPDaoJdbcImpl;
import java.sql.*;
import edu.univ.erp.auth.JBCryptPasswordHasher;

public class AdminServiceJdbcImpl implements AdminService {
    private final ERPDaoJdbcImpl dao;
    public AdminServiceJdbcImpl(ERPDaoJdbcImpl dao) { this.dao = dao; }
    @Override
    public void createCourse(String code, String title, int credits) {
        long id = dao.createCourse(code, title, credits);
    }
    @Override
    public void createSection(long courseId, String dayTime, String room, int capacity, String semester, int year) {
        dao.createSection(courseId, dayTime, room, capacity, semester, year);
    }
    @Override
    public void toggleMaintenance(boolean on) {
        dao.setMaintenance(on);
    }

    @Override
    public void createUser(String username, String password, String role, String extraInfo) {
        // 1. Create Auth User
        String hash = new edu.univ.erp.auth.JBCryptPasswordHasher().hash(password);
        long userId = -1;
        
        // (Simplification: We use raw JDBC here because adding a method to DAO takes longer steps)
        // In strict architecture, move this SQL to ERPDaoJdbcImpl
        try (java.sql.Connection c = edu.univ.erp.data.ConnectionFactory.authDataSource().getConnection()) {
            var ps = c.prepareStatement("INSERT INTO users_auth (username, role, password_hash) VALUES (?,?,?)", java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, role.toLowerCase());
            ps.setString(3, hash);
            ps.executeUpdate();
            var rs = ps.getGeneratedKeys();
            if(rs.next()) userId = rs.getLong(1);
        } catch(Exception e) { e.printStackTrace(); return; }

        // 2. Create Profile in ERP DB
        if(userId != -1) {
            try (java.sql.Connection c = edu.univ.erp.data.ConnectionFactory.erpDataSource().getConnection()) {
                if("student".equalsIgnoreCase(role)) {
                    var ps = c.prepareStatement("INSERT INTO students (user_id, roll_no, program, year) VALUES (?, ?, 'CS', 1)");
                    ps.setLong(1, userId);
                    ps.setString(2, extraInfo); // Use extraInfo as RollNo
                    ps.executeUpdate();
                } else if ("instructor".equalsIgnoreCase(role)) {
                    var ps = c.prepareStatement("INSERT INTO instructors (user_id, department) VALUES (?, ?)");
                    ps.setLong(1, userId);
                    ps.setString(2, extraInfo); // Use extraInfo as Dept
                    ps.executeUpdate();
                }
            } catch(Exception e) { e.printStackTrace(); }
        }
    }
}
