package edu.univ.erp.service;

import edu.univ.erp.data.ERPDaoJdbcImpl;
import edu.univ.erp.domain.Enrollment;
import java.util.*;
import java.sql.*;
import java.io.File;
import edu.univ.erp.util.CsvUtil;
import edu.univ.erp.util.PdfUtil;

public class StudentServiceJdbcImpl implements StudentService {
    private final ERPDaoJdbcImpl dao;
    public StudentServiceJdbcImpl(ERPDaoJdbcImpl dao) { this.dao = dao; }

    @Override
    public void registerSection(long studentId, long sectionId) throws Exception {
        if(dao.isMaintenanceOn()) throw new Exception("Maintenance is ON. Registration disabled.");
        // deadline check
        SectionInfo s = dao.getSectionInfo(sectionId);
        if(s == null) throw new Exception("Section not found");
        Date deadline = s.getEnrollmentDeadline();
        if(deadline != null && new Date().after(deadline)) throw new Exception("Enrollment deadline passed");
        int enrolled = dao.countActiveEnrollmentsInSection(sectionId);
        if(enrolled >= s.getCapacity()) throw new Exception("Section full");
        if(dao.studentHasActiveEnrollment(studentId, sectionId)) throw new Exception("Already registered in that section");
        dao.createEnrollmentJdbc(studentId, sectionId);
    }

    @Override
    public void dropSection(long studentId, long sectionId) throws Exception {
        if(dao.isMaintenanceOn()) throw new Exception("Maintenance is ON. Drop disabled.");
        // allow drop before deadline only
        SectionInfo s = dao.getSectionInfo(sectionId);
        if(s == null) throw new Exception("Section not found");
        Date deadline = s.getEnrollmentDeadline();
        if(deadline != null && new Date().after(deadline)) throw new Exception("Drop deadline passed");
        dao.dropEnrollmentJdbc(studentId, sectionId);
    }

    @Override
    public java.util.List<Enrollment> myEnrollments(long studentId) {
        return dao.listEnrollmentsByStudentJdbc(studentId);
    }

    // transcript export (CSV)
    public File exportTranscriptCsv(long studentId, String filename) throws Exception {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"Course","Component","Score","Final"});
        java.util.List<Map<String,Object>> recs = dao.getGradesForStudent(studentId);
        for(var r: recs) {
            rows.add(new String[]{String.valueOf(r.get("course_code")), String.valueOf(r.get("component")), String.valueOf(r.get("score")), String.valueOf(r.get("final_grade"))});
        }
        return CsvUtil.writeCsv(filename, rows);
    }
}
