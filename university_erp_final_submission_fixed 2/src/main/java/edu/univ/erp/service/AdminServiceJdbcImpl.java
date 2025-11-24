package edu.univ.erp.service;

import edu.univ.erp.data.ERPDaoJdbcImpl;

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
}
