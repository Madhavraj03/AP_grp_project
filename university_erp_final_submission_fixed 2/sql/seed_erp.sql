-- seed_erp.sql
USE erp_db;
INSERT INTO students (user_id, roll_no, program, year) VALUES
(3, '2023001', 'CS', 2),
(4, '2023002', 'CS', 2) ON DUPLICATE KEY UPDATE roll_no=VALUES(roll_no);
INSERT INTO instructors (user_id, department) VALUES
(2, 'CSE') ON DUPLICATE KEY UPDATE department=VALUES(department);
INSERT INTO courses (id, code, title, credits) VALUES
(10, 'CS101', 'Intro to CS', 3),
(11, 'MA101', 'Calculus I', 4) ON DUPLICATE KEY UPDATE code=VALUES(code);
INSERT INTO sections (id, course_id, instructor_id, day_time, room, capacity, semester, year, enrollment_deadline) VALUES
(20, 10, 2, 'Mon 10:00-11:00', 'R101', 2, 'Fall', 2025, DATE_ADD(CURDATE(), INTERVAL 30 DAY)),
(21, 11, 2, 'Tue 11:00-12:00', 'R102', 1, 'Fall', 2025, DATE_ADD(CURDATE(), INTERVAL 30 DAY))
ON DUPLICATE KEY UPDATE day_time=VALUES(day_time);
INSERT INTO settings (`key`,`value`) VALUES ('maintenance','false') ON DUPLICATE KEY UPDATE `value`=VALUES(`value`);
