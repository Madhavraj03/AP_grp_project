-- erp_schema.sql
CREATE DATABASE IF NOT EXISTS erp_db;
USE erp_db;
CREATE TABLE IF NOT EXISTS students (
  user_id BIGINT PRIMARY KEY,
  roll_no VARCHAR(50),
  program VARCHAR(100),
  year INT
);
CREATE TABLE IF NOT EXISTS instructors (
  user_id BIGINT PRIMARY KEY,
  department VARCHAR(100)
);
CREATE TABLE IF NOT EXISTS courses (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(50) NOT NULL UNIQUE,
  title VARCHAR(255) NOT NULL,
  credits INT NOT NULL
);
CREATE TABLE IF NOT EXISTS sections (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  course_id BIGINT NOT NULL,
  instructor_id BIGINT,
  day_time VARCHAR(100),
  room VARCHAR(100),
  capacity INT NOT NULL,
  semester VARCHAR(50),
  year INT,
  enrollment_deadline DATE,
  FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS enrollments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  student_id BIGINT NOT NULL,
  section_id BIGINT NOT NULL,
  status VARCHAR(20) DEFAULT 'ACTIVE',
  UNIQUE KEY ux_student_section (student_id, section_id)
);
CREATE TABLE IF NOT EXISTS grades (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  enrollment_id BIGINT NOT NULL,
  component VARCHAR(100),
  score DOUBLE,
  final_grade DOUBLE,
  FOREIGN KEY (enrollment_id) REFERENCES enrollments(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS settings (
  `key` VARCHAR(100) PRIMARY KEY,
  `value` VARCHAR(255)
);
