-- Create database if no one exist
CREATE DATABASE IF NOT EXISTS smartslate_database;

-- Use databaase
USE smartslate_database;

-- Drop tables if they exist
DROP TABLE IF EXISTS SubTasks;
DROP TABLE IF EXISTS employeeTasks;
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
    titel       VARCHAR(255) NOT NULL,
    Description VARCHAR(1000),
    StartDate   DATE         NOT NULL,
    EndDate     DATE,
    Budget      DECIMAL(10, 2),
    Status      VARCHAR(20)  NOT NULL,
    PRIMARY KEY (ProjectID),
    FOREIGN KEY (UserID) REFERENCES Users (UserID)
);

-- Når ON DELETE CASCADE er angivet for en fremmednøgle i en tabel,
-- betyder det, at hvis en række i den tilsvarende primærnøgletabel slettes,
-- vil alle de tilknyttede rækker i den tilknyttede tabel også blive slettet automatisk.
CREATE TABLE Tasks
(
    TaskID      INTEGER       NOT NULL AUTO_INCREMENT,
    ProjectID   INTEGER       NOT NULL,
    Description VARCHAR(1000) NOT NULL,
    Deadline    DATE,
    AssignedTo  VARCHAR(255),
    Status      VARCHAR(20)   NOT NULL,
    PRIMARY KEY (TaskID),
    FOREIGN KEY (ProjectID) REFERENCES Projects (ProjectID)
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
    CONSTRAINT fk_task FOREIGN KEY (TaskID) REFERENCES Tasks (TaskID) ON DELETE CASCADE
);

CREATE TABLE employeeTasks
(
    EmployeeTaskID INTEGER NOT NULL AUTO_INCREMENT,
    UserID         INTEGER NOT NULL,
    TaskID         INTEGER NOT NULL,
    Hours          DECIMAL(10, 2),
    PRIMARY KEY (EmployeeTaskID),
    FOREIGN KEY (UserID) REFERENCES Users (UserID),
    FOREIGN KEY (TaskID) REFERENCES Tasks (TaskID) ON DELETE CASCADE
);

