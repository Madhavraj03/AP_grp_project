package edu.univ.erp.data;

import edu.univ.erp.domain.*;
import java.sql.*;
import java.util.*;
import javax.sql.DataSource;

public class ERPDaoJdbcImpl {
    private final DataSource authDs;
    private final DataSource erpDs;

    public ERPDaoJdbcImpl(DataSource authDs, DataSource erpDs) {
        this.authDs = authDs; this.erpDs = erpDs;
    }

    public UserAuth getUserAuthByUsername(String username) {
        String sql = "SELECT user_id, username, role, password_hash, status, last_login FROM users_auth WHERE username = ?";
        try (Connection c = authDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserAuth u = new UserAuth(rs.getLong("user_id"), rs.getString("username"), rs.getString("role"), rs.getString("password_hash"));
                    u.setActive("active".equalsIgnoreCase(rs.getString("status")));
                    Timestamp t = rs.getTimestamp("last_login");
                    if (t != null) u.setLastLogin(t.getTime());
                    return u;
                }
            }
        } catch(Exception e) { e.printStackTrace(); }
        return null;
    }

    // courses
    public List<Course> listCourses() {
        List<Course> out = new ArrayList<>();
        String sql = "SELECT id, code, title, credits FROM courses";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while(rs.next()) out.add(new Course(rs.getLong("id"), rs.getString("code"), rs.getString("title"), rs.getInt("credits")));
        } catch(Exception e) { e.printStackTrace(); }
        return out;
    }
    // section info DTO
    public static class SectionInfo { public long id; public int capacity; public java.util.Date enrollmentDeadline; public String dayTime; public SectionInfo(long id,int cap,java.util.Date d,String dt){this.id=id;this.capacity=cap;this.enrollmentDeadline=d;this.dayTime=dt;} }
    public SectionInfo getSectionInfo(long sectionId) {
        String sql = "SELECT id, capacity, enrollment_deadline, day_time FROM sections WHERE id = ?";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, sectionId);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    Timestamp t = rs.getTimestamp("enrollment_deadline");
                    java.util.Date d = t==null?null:new java.util.Date(t.getTime());
                    return new SectionInfo(rs.getLong("id"), rs.getInt("capacity"), d, rs.getString("day_time"));
                }
            }
        } catch(Exception e) { e.printStackTrace(); }
        return null;
    }

    public int countActiveEnrollmentsInSection(long sectionId) {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE section_id = ? AND status = 'ACTIVE'";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, sectionId);
            try (ResultSet rs = ps.executeQuery()) { if(rs.next()) return rs.getInt(1); }
        } catch(Exception e) { e.printStackTrace(); }
        return 0;
    }

    public boolean studentHasActiveEnrollment(long studentId, long sectionId) {
        String sql = "SELECT 1 FROM enrollments WHERE student_id = ? AND section_id = ? AND status = 'ACTIVE' LIMIT 1";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, studentId); ps.setLong(2, sectionId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch(Exception e) { e.printStackTrace(); }
        return false;
    }

    public void createEnrollmentJdbc(long studentId, long sectionId) throws SQLException {
        String sql = "INSERT INTO enrollments (student_id, section_id, status) VALUES (?, ?, 'ACTIVE')";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, studentId); ps.setLong(2, sectionId); ps.executeUpdate();
        }
    }

    public void dropEnrollmentJdbc(long studentId, long sectionId) throws SQLException {
        String sql = "UPDATE enrollments SET status='DROPPED' WHERE student_id = ? AND section_id = ? AND status='ACTIVE'";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, studentId); ps.setLong(2, sectionId); ps.executeUpdate();
        }
    }

    // Grades helpers (simplified)
    public List<edu.univ.erp.domain.Grade> getGradesForSection(long sectionId) {
        List<edu.univ.erp.domain.Grade> out = new ArrayList<>();
        String sql = "SELECT g.id, g.enrollment_id, g.component, g.score, g.final_grade FROM grades g JOIN enrollments e ON g.enrollment_id=e.id WHERE e.section_id = ?";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, sectionId);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) out.add(new edu.univ.erp.domain.Grade(rs.getLong("id"), rs.getLong("enrollment_id"), rs.getString("component"), rs.getDouble("score")));
            }
        } catch(Exception e) { e.printStackTrace(); }
        return out;
    }

    public List<edu.univ.erp.domain.Grade> getGradesForEnrollment(long enrollmentId) {
        List<edu.univ.erp.domain.Grade> out = new ArrayList<>();
        String sql = "SELECT id, enrollment_id, component, score, final_grade FROM grades WHERE enrollment_id = ?";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, enrollmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    var g = new edu.univ.erp.domain.Grade(rs.getLong("id"), rs.getLong("enrollment_id"), rs.getString("component"), rs.getDouble("score"));
                    g.setFinalGrade(rs.getDouble("final_grade"));
                    out.add(g);
                }
            }
        } catch(Exception e) { e.printStackTrace(); }
        return out;
    }

    public void saveGradeJdbc(edu.univ.erp.domain.Grade grade) {
        String sql = "INSERT INTO grades (enrollment_id, component, score) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE score=VALUES(score)";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, grade.getEnrollmentId()); ps.setString(2, grade.getComponent()); ps.setDouble(3, grade.getScore()); ps.executeUpdate();
        } catch(Exception e) { e.printStackTrace(); }
    }

    public void updateFinalGrade(long enrollmentId, double finalGrade) {
        String sql = "UPDATE grades SET final_grade = ? WHERE enrollment_id = ?";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, finalGrade); ps.setLong(2, enrollmentId); ps.executeUpdate();
        } catch(Exception e) { e.printStackTrace(); }
    }

    public java.util.List<java.util.Map<String,Object>> getGradesForSectionAsMaps(long sectionId) {
        List<java.util.Map<String,Object>> out = new ArrayList<>();
        String sql = "SELECT e.student_id as student_id, g.component, g.score, g.final_grade FROM grades g JOIN enrollments e ON g.enrollment_id = e.id WHERE e.section_id = ?";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, sectionId);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    Map<String,Object> m = new HashMap<>();
                    m.put("student_id", rs.getLong("student_id")); m.put("component", rs.getString("component")); m.put("score", rs.getDouble("score")); m.put("final_grade", rs.getDouble("final_grade"));
                    out.add(m);
                }
            }
        } catch(Exception e) { e.printStackTrace(); }
        return out;
    }

    public java.util.List<Double> getFinalGradesForSection(long sectionId) {
        List<Double> out = new ArrayList<>();
        String sql = "SELECT DISTINCT e.student_id, g.final_grade FROM grades g JOIN enrollments e ON g.enrollment_id = e.id WHERE e.section_id = ? AND g.final_grade IS NOT NULL";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, sectionId);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) out.add(rs.getDouble("final_grade"));
            }
        } catch(Exception e) { e.printStackTrace(); }
        return out;
    }

    // create course/section minimal implementations
    public long createCourse(String code, String title, int credits) {
        String sql = "INSERT INTO courses (code, title, credits) VALUES (?, ?, ?)";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, code); ps.setString(2, title); ps.setInt(3, credits); ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if(rs.next()) return rs.getLong(1); }
        } catch(Exception e) { e.printStackTrace(); }
        return -1;
    }

    public void createSection(long courseId, String dayTime, String room, int capacity, String semester, int year) {
        String sql = "INSERT INTO sections (course_id, day_time, room, capacity, semester, year, enrollment_deadline) VALUES (?, ?, ?, ?, ?, ?, DATE_ADD(CURDATE(), INTERVAL 30 DAY))";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, courseId); ps.setString(2, dayTime); ps.setString(3, room); ps.setInt(4, capacity); ps.setString(5, semester); ps.setInt(6, year); ps.executeUpdate();
        } catch(Exception e) { e.printStackTrace(); }
    }

    public boolean isMaintenanceOn() {
        String sql = "SELECT `value` FROM settings WHERE `key`='maintenance' LIMIT 1";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return "true".equalsIgnoreCase(rs.getString(1));
        } catch(Exception e) { e.printStackTrace(); }
        return false;
    }

    public void setMaintenance(boolean on) {
        String sql = "INSERT INTO settings (`key`, `value`) VALUES ('maintenance', ?) ON DUPLICATE KEY UPDATE `value`=VALUES(`value`)";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, on ? "true" : "false"); ps.executeUpdate();
        } catch(Exception e) { e.printStackTrace(); }
    }
}

    // Additional simple DTOs and methods used by UI
    public static class SectionRow { public long id; public String courseCode; public String dayTime; public int capacity; public SectionRow(long id,String cc,String dt,int cap){this.id=id;this.courseCode=cc;this.dayTime=dt;this.capacity=cap;} }
    public java.util.List<SectionRow> listSectionsSimple() {
        List<SectionRow> out = new ArrayList<>();
        String sql = "SELECT s.id, c.code, s.day_time, s.capacity FROM sections s JOIN courses c ON s.course_id=c.id";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while(rs.next()) out.add(new SectionRow(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
        } catch(Exception e) { e.printStackTrace(); }
        return out;
    }
    public java.util.List<Section> getSectionsByInstructor(long instructorId) {
        List<Section> out = new ArrayList<>();
        String sql = "SELECT id, course_id, instructor_id, day_time, room, capacity, semester, year FROM sections WHERE instructor_id = ?";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, instructorId);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) out.add(new Section(rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getString(7), rs.getInt(8)));
            }
        } catch(Exception e) { e.printStackTrace(); }
        return out;
    }
}
