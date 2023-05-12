-- Insert roles
INSERT INTO roles (roleName)
VALUES ('admin'),
       ('project manager'),
       ('employee');

-- Insert users
INSERT INTO users (username, password, email, firstName, lastName, phoneNumber, roleID)
VALUES ('Malou', '1234', 'malou@example.com', 'Malou', 'Lundstrøm', '12345678', 1),
       ('Magnus', '1234', 'magnus@example.com', 'Magnus', 'Korse', '87654321', 2),
       ('Heval', '1234', 'heval@example.com', 'Heval', 'Tirpan', '11223344', 3);

-- Insert projects
INSERT INTO projects (userID, projectName, description, startDate, endDate, budget, status)
VALUES (2, 'Project A', 'This is project A', '2023-01-01', '2023-12-31', 100000, 'Planning'),
       (2, 'Project B', 'This is project B', '2023-02-01', '2023-11-30', 50000, 'Planning'),
       (1, 'Project C', 'This is project C', '2023-03-01', '2023-10-31', 75000, 'Planning');

-- Insert tasks
INSERT INTO tasks (projectID, taskName, description, deadline, projectManagerID, status)
VALUES (1, 'Task 1A', 'This is task 1A for Project A', '2023-01-31', NULL, 'Planning'),
       (1, 'Task 2A', 'This is task 2A for Project A', '2023-02-28', 2, 'Planning'),
       (2, 'Task 1B', 'This is task 1B for Project B', '2023-03-31', NULL, 'Planning');

-- Insert employeeTasks
INSERT INTO employeeTasks (taskEmployeeID, taskID, hours)
VALUES (3, 1, 20), -- assign 20 hours for employee with userID 3 to task with taskID 1
       (3, 2, 15); -- assign 15 hours for employee with userID 3 to task with taskID 2
