-- Create database if one doesn't exist
DROP DATABASE IF EXISTS smartslate_db;
CREATE DATABASE IF NOT EXISTS smartslate_db;

-- Use databaase
USE smartslate_db;

-- Drop tables if they exist
DROP TABLE IF EXISTS project;
DROP TABLE IF EXISTS user;

-- Create new tables
CREATE TABLE user (
                      user_id INTEGER NOT NULL AUTO_INCREMENT,
                      first_name VARCHAR(20),
                      last_name VARCHAR(20),
                      user_email VARCHAR(50) UNIQUE,
                      user_password VARCHAR(50),
                      PRIMARY KEY (user_id)
);

CREATE TABLE project (
                         project_id INTEGER NOT NULL AUTO_INCREMENT,
                         project_title VARCHAR(50),
                         project_description VARCHAR(255),
                         project_url VARCHAR(2083),
                         project_picture  BLOB,
                         user_id INTEGER,
                         PRIMARY KEY (project_id),
                         FOREIGN KEY (user_id) REFERENCES user (user_id)
);