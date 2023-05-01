-- Create database if no one exist
CREATE DATABASE IF NOT EXISTS smartslate_database;

-- Use databaase
USE smartslate_database;

-- Drop tables if they exist
DROP TABLE IF EXISTS employeeTasks;
DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS users;


DROP TABLE IF EXISTS employees;



-- Create new tables
CREATE TABLE Users (
  UserID INT PRIMARY KEY,
  Username VARCHAR(255) NOT NULL,
  Password VARCHAR(255) NOT NULL,
  Email VARCHAR(255),
  FirstName VARCHAR(255),
  LastName VARCHAR(255),
  CreatedAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UpdatedAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE Projects (
  ProjectID INT PRIMARY KEY,
  UserID INT NOT NULL,
  Name VARCHAR(255) NOT NULL,
  Description VARCHAR(1000),
  StartDate DATE NOT NULL,
  EndDate DATE,
  Budget DECIMAL(10, 2), 
  Status VARCHAR(20) NOT NULL,
  FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Når ON DELETE CASCADE er angivet for en fremmednøgle i en tabel, 
-- betyder det, at hvis en række i den tilsvarende primærnøgletabel slettes, 
-- vil alle de tilknyttede rækker i den tilknyttede tabel også blive slettet automatisk.
CREATE TABLE Tasks (
  TaskID INT PRIMARY KEY,
  ProjectID INT NOT NULL,
  Name VARCHAR(255) NOT NULL,
  Description VARCHAR(1000),
  StartDate DATE NOT NULL,
  EndDate DATE,
  Budget DECIMAL(10, 2),
  Status VARCHAR(20) NOT NULL,
  CONSTRAINT fk_project FOREIGN KEY (ProjectID) 
  REFERENCES Projects(ProjectID) ON DELETE CASCADE
);

CREATE TABLE Employees (
  EmployeeID INT PRIMARY KEY,
  UserName VARCHAR(255) NOT NULL,
  FirstName VARCHAR(255) NOT NULL,
  LastName VARCHAR(255) NOT NULL,
  employee_password VARCHAR(255) NOT NULL,
  Email VARCHAR(255),
  Phone VARCHAR(20)
);

CREATE TABLE EmployeeTasks (
EmployeeID INT NOT NULL,
TaskID INT NOT NULL,
CONSTRAINT pk_employee_task
PRIMARY KEY (EmployeeID, TaskID),
CONSTRAINT fk_employee
FOREIGN KEY (EmployeeID)
REFERENCES Employees(EmployeeID) ON DELETE CASCADE,
CONSTRAINT fk_task
FOREIGN KEY (TaskID)
REFERENCES Tasks(TaskID) ON DELETE CASCADE
);
