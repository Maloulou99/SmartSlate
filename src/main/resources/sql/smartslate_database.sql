-- Create database if not exists
CREATE DATABASE IF NOT EXISTS smartslate_database;

-- Use database
USE smartslate_database;

-- Drop tables if they exist
DROP TABLE IF EXISTS employeeTasks;
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
    username     VARCHAR(20) UNIQUE NOT NULL,
    password     VARCHAR(20)        NOT NULL,
    email        VARCHAR(20) UNIQUE,
    firstName    VARCHAR(20),
    lastName     VARCHAR(20),
    phoneNumber VARCHAR(20),
    createdAt    DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt    DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    roleID       INTEGER             NOT NULL,
    PRIMARY KEY (userID),
    FOREIGN KEY (roleID) REFERENCES roles(roleID)
) auto_increment = 1;


CREATE TABLE projects
(
    projectID        INTEGER      NOT NULL AUTO_INCREMENT,
    userID 			 INTEGER,
    projectName      VARCHAR(20) NOT NULL,
    description      VARCHAR(1000),
    startDate        DATE         NOT NULL,
    endDate          DATE,
    budget           DECIMAL(10, 2),
    status           VARCHAR(20)  NOT NULL,
    PRIMARY KEY (projectID),
    FOREIGN KEY (userID) REFERENCES users (userID) ON DELETE SET NULL
);

CREATE TABLE tasks
(
    taskID           INTEGER      NOT NULL AUTO_INCREMENT,
    projectID        INTEGER      NOT NULL,
    taskName         VARCHAR(50)  NOT NULL,
    description      VARCHAR(1000),
    deadline         VARCHAR(10),
    projectManagerID INTEGER,
    userID           INTEGER, -- Tilføjet kolonne for userID
    status           VARCHAR(20)  NOT NULL,
    PRIMARY KEY (taskID),
    FOREIGN KEY (projectID) REFERENCES projects(projectID),
    FOREIGN KEY (projectManagerID) REFERENCES users (userID),
    FOREIGN KEY (userID) REFERENCES users (userID) -- Tilføjet fremmednøgle for userID
);

CREATE TABLE employeeTasks
(
    employeeTaskID INTEGER NOT NULL AUTO_INCREMENT,
    taskEmployeeID INTEGER,
    taskID         INTEGER NOT NULL,
    hours          DECIMAL(10, 2),
    PRIMARY KEY (employeeTaskID),
    FOREIGN KEY (taskEmployeeID) REFERENCES users(userID) ON DELETE SET NULL,
    FOREIGN KEY (taskID) REFERENCES tasks(taskID)
) AUTO_INCREMENT = 1;



