package edu.univ.erp.service;

import edu.univ.erp.data.ERPDaoJdbcImpl;
import java.util.*;
import java.io.File;
import edu.univ.erp.util.CsvUtil;

public class InstructorServiceJdbcImpl implements InstructorService {
    private final ERPDaoJdbcImpl dao;
    public InstructorServiceJdbcImpl(ERPDaoJdbcImpl dao) { this.dao = dao; }

    @Override
    public java.util.List<edu.univ.erp.domain.Grade> getGradesForSection(long sectionId) {
        return dao.getGradesForSection(sectionId);
    }

    @Override
    public void saveGrade(edu.univ.erp.domain.Grade grade) {
        dao.saveGradeJdbc(grade);
    }

    // compute final grade for an enrollment using 20/30/50 weights for components named quiz, midterm, endsem
    public double computeFinalForEnrollment(long enrollmentId) {
        java.util.List<edu.univ.erp.domain.Grade> comps = dao.getGradesForEnrollment(enrollmentId);
        double quiz = 0, mid = 0, end = 0;
        for(var g: comps) {
            if("quiz".equalsIgnoreCase(g.getComponent())) quiz = g.getScore();
            if("midterm".equalsIgnoreCase(g.getComponent())) mid = g.getScore();
            if("endsem".equalsIgnoreCase(g.getComponent())) end = g.getScore();
        }
        double finalGrade = quiz*0.2 + mid*0.3 + end*0.5;
        dao.updateFinalGrade(enrollmentId, finalGrade);
        return finalGrade;
    }

    public File exportGradesCsv(long sectionId, String filename) throws Exception {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"StudentId","Component","Score","FinalGrade"});
        List<Map<String,Object>> recs = dao.getGradesForSectionAsMaps(sectionId);
        for(var r: recs) rows.add(new String[]{String.valueOf(r.get("student_id")), String.valueOf(r.get("component")), String.valueOf(r.get("score")), String.valueOf(r.get("final_grade"))});
        return CsvUtil.writeCsv(filename, rows);
    }

    public Map<String, Double> simpleStats(long sectionId) {
        java.util.List<Double> finals = dao.getFinalGradesForSection(sectionId);
        double sum=0; for(double v: finals) sum+=v;
        double avg = finals.isEmpty()?0:sum/finals.size();
        double sd=0;
        for(double v: finals) sd += (v-avg)*(v-avg);
        sd = finals.size()>1?Math.sqrt(sd/(finals.size()-1)):0;
        Map<String,Double> m = new HashMap<>(); m.put("avg", avg); m.put("sd", sd);
        return m;
    }
}
