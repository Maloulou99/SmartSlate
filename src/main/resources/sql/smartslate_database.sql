-- Create database if not exists
CREATE DATABASE IF NOT EXISTS smartslate_database;

-- Use database
USE smartslate_database;

-- Drop tables if they exist
DROP TABLE IF EXISTS employeeTasks;
DROP TABLE IF EXISTS SubTasks;
DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS users;

-- Create new tables
CREATE TABLE Users
(
    UserID      INTEGER             NOT NULL AUTO_INCREMENT,
    Username    VARCHAR(255) UNIQUE NOT NULL,
    Password    VARCHAR(255)        NOT NULL,
    Email       VARCHAR(255) UNIQUE,
    FirstName   VARCHAR(255),
    LastName    VARCHAR(255),
    Role        VARCHAR(30),
    PhoneNumber VARCHAR(20),
    CreatedAt   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (UserID)
);

CREATE TABLE Projects
(
    ProjectID   INTEGER      NOT NULL AUTO_INCREMENT,
    UserID      INTEGER      NOT NULL,
    ProjectName       VARCHAR(255) NOT NULL,
    Description VARCHAR(1000),
    StartDate   DATE         NOT NULL,
    EndDate     DATE,
    Budget      DECIMAL(10, 2),
    Status      VARCHAR(20)  NOT NULL,
    PRIMARY KEY (ProjectID),
    FOREIGN KEY (UserID) REFERENCES Users (UserID) ON DELETE CASCADE
);

CREATE TABLE Tasks
(
    TaskID      INTEGER       NOT NULL AUTO_INCREMENT,
    ProjectID   INTEGER       NOT NULL,
    Description VARCHAR(1000) NOT NULL,
    Deadline    DATE,
    AssignedTo  VARCHAR(255),
    Status      VARCHAR(20)   NOT NULL,
    PRIMARY KEY (TaskID),
    FOREIGN KEY (ProjectID) REFERENCES Projects (ProjectID) ON DELETE CASCADE
);

CREATE TABLE SubTasks
(
    SubTaskID   INTEGER      NOT NULL AUTO_INCREMENT,
    TaskID      INTEGER      NOT NULL,
    Name        VARCHAR(255) NOT NULL,
    Description VARCHAR(1000),
    StartDate   DATE         NOT NULL,
    EndDate     DATE,
    Budget      DECIMAL(10, 2),
    Status      VARCHAR(20)  NOT NULL,
    PRIMARY KEY (SubTaskID),
    FOREIGN KEY (TaskID) REFERENCES Tasks (TaskID) ON DELETE CASCADE
);

CREATE TABLE employeeTasks
(
    EmployeeTaskID INTEGER NOT NULL AUTO_INCREMENT,
    UserID         INTEGER NOT NULL,
    TaskID         INTEGER NOT NULL,
    Hours          DECIMAL(10, 2),
    PRIMARY KEY (EmployeeTaskID),
    FOREIGN KEY (UserID) REFERENCES Users (UserID) ON DELETE CASCADE,
    FOREIGN KEY (TaskID) REFERENCES Tasks (TaskID) ON DELETE CASCADE
);
