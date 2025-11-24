package edu.univ.erp.data;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource;
import edu.univ.erp.domain.Enrollment;

public class ERPDaoJdbcExtras {
    private final DataSource erpDs;
    public ERPDaoJdbcExtras(DataSource erpDs) { this.erpDs = erpDs; }

    public java.util.List<Enrollment> listEnrollmentsByStudentJdbc(long studentId) {
        List<Enrollment> out = new ArrayList<>();
        String sql = "SELECT id, student_id, section_id, status FROM enrollments WHERE student_id = ? AND status='ACTIVE'";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) out.add(new Enrollment(rs.getLong("id"), rs.getLong("student_id"), rs.getLong("section_id"), rs.getString("status")));
            }
        } catch(Exception e) { e.printStackTrace(); }
        return out;
    }

    public java.util.List<Map<String,Object>> getGradesForStudent(long studentId) {
        List<Map<String,Object>> out = new ArrayList<>();
        String sql = "SELECT c.code as course_code, g.component, g.score, g.final_grade FROM grades g JOIN enrollments e ON g.enrollment_id=e.id JOIN sections s ON e.section_id=s.id JOIN courses c ON s.course_id=c.id WHERE e.student_id=?";
        try (Connection c = erpDs.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    Map<String,Object> m = new HashMap<>();
                    m.put("course_code", rs.getString("course_code"));
                    m.put("component", rs.getString("component"));
                    m.put("score", rs.getDouble("score"));
                    m.put("final_grade", rs.getDouble("final_grade"));
                    out.add(m);
                }
            }
        } catch(Exception e) { e.printStackTrace(); }
        return out;
    }
}
