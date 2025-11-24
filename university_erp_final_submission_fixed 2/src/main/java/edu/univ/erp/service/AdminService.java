package edu.univ.erp.service;

public interface AdminService {
    void createCourse(String code, String title, int credits);
    void createSection(long courseId, String dayTime, String room, int capacity, String semester, int year);
    void toggleMaintenance(boolean on);
    void createUser(String username, String password, String role, String extraInfo);
}

