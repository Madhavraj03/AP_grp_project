-- auth_schema.sql
CREATE DATABASE IF NOT EXISTS auth_db;
USE auth_db;
CREATE TABLE IF NOT EXISTS users_auth (
  user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(100) NOT NULL UNIQUE,
  role ENUM('admin','instructor','student') NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  status VARCHAR(20) DEFAULT 'active',
  last_login TIMESTAMP NULL DEFAULT NULL
);
