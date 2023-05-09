-- Create database if not exists
CREATE DATABASE IF NOT EXISTS smartslate_database;

-- Use database
USE smartslate_database;

-- Drop tables if they exist
DROP TABLE IF EXISTS employeetasks;
DROP TABLE IF EXISTS subtasks;
DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;


CREATE TABLE roles
(
    roleID    INTEGER NOT NULL AUTO_INCREMENT,
    roleName VARCHAR(30) NOT NULL,
    PRIMARY KEY (roleID)
);

-- Create new tables
CREATE TABLE users
(
    userID       INTEGER             NOT NULL AUTO_INCREMENT,
    username     VARCHAR(255) UNIQUE NOT NULL,
    password     VARCHAR(255)        NOT NULL,
    email        VARCHAR(255) UNIQUE,
    firstName    VARCHAR(255),
    lastName     VARCHAR(255),
    phoneNumber VARCHAR(20),
    createdAt    DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt    DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    roleID       INTEGER             NOT NULL,
    PRIMARY KEY (userID),
    FOREIGN KEY (roleID) REFERENCES roles(roleID)
);


CREATE TABLE projects
(
    projectID        INTEGER      NOT NULL AUTO_INCREMENT,
    projectManagerID INTEGER      NOT NULL,
    projectName      VARCHAR(255) NOT NULL,
    description      VARCHAR(1000),
    startDate        DATE         NOT NULL,
    endDate          DATE,
    budget           DECIMAL(10, 2),
    status           VARCHAR(20)  NOT NULL,
    PRIMARY KEY (projectID),
    FOREIGN KEY (projectManagerID) REFERENCES users (userID)
);

CREATE TABLE tasks
(
    taskID       INTEGER       NOT NULL AUTO_INCREMENT,
    projectID    INTEGER       NOT NULL,
    description  VARCHAR(1000) NOT NULL,
    deadline     DATE,
    assignedTo   INTEGER,
    status       VARCHAR(20)   NOT NULL,
    PRIMARY KEY (taskID),
    FOREIGN KEY (projectID) REFERENCES projects (projectID) ON DELETE CASCADE,
    FOREIGN KEY (assignedTo) REFERENCES users (userID)
);

CREATE TABLE subtasks
(
    subtaskID     INTEGER      NOT NULL AUTO_INCREMENT,
    taskID        INTEGER      NOT NULL,
    subtaskName   VARCHAR(255) NOT NULL,
    description   VARCHAR(1000),
    startDate     DATE         NOT NULL,
    endDate       DATE,
    budget        DECIMAL(10, 2),
    status        VARCHAR(20)  NOT NULL,
    PRIMARY KEY (subtaskID),
    FOREIGN KEY (taskID) REFERENCES tasks (taskID) ON DELETE CASCADE
);

CREATE TABLE employeeTasks
(
    employeeTaskID   INTEGER NOT NULL AUTO_INCREMENT,
    taskEmployeeID   INTEGER,
    taskID           INTEGER NOT NULL,
    hours            DECIMAL(10, 2),
    PRIMARY KEY (employeeTaskID),
    FOREIGN KEY (taskEmployeeID) REFERENCES users (userID) ON DELETE CASCADE,
    FOREIGN KEY (taskID) REFERENCES tasks (taskID) ON DELETE CASCADE
);


