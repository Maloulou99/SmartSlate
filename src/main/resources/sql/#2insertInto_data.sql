-- Insert roles
INSERT INTO roles (roleName)
VALUES ('admin'),
       ('project manager'),
       ('employee');

-- Insert users
INSERT INTO users (username, password, email, firstName, lastName, phoneNumber, roleID)
VALUES ('Malou', '1234', 'malou@example.com', 'Malou', 'Lundstrøm', '12345678', 1),
       ('Magnus', '1234', 'magnus@example.com', 'Magnus', 'Korse', '87654321', 2),
       ('Heval', '1234', 'heval@example.com', 'Heval', 'Tirpan', '11223344', 3),
       ('Mikkel', '1234', 'Mikkel@example.com', 'Mikkel', 'Shultz', '11233344', 3);

-- Insert projects
INSERT INTO projects (userID, projectName, description, startDate, endDate, budget, status)
VALUES (2, 'Project A', 'This is project A', '2023-01-01', '2023-12-31', 100000, 'Planning'),
       (2, 'Project B', 'This is project B', '2023-02-01', '2023-11-30', 50000, 'Planning'),
       (1, 'Project C', 'This is project C', '2023-03-01', '2023-10-31', 75000, 'Planning');

-- Insert tasks
INSERT INTO tasks (projectID, taskName, description, hours, projectManagerID, userID, status)
VALUES (1, 'Udvikle hjemmeside', 'Design og udvikling af en responsiv hjemmeside', 11.00, 2, 3, 'Planning');
INSERT INTO tasks (projectID, taskName, description, hours, projectManagerID, userID, status)
VALUES (2, 'Færdiggør dokumentation', 'Skriv de sidste afsnit af dokumentationen', 11.30, 2, 3, 'Planning');


-- Insert employeeTasks
INSERT INTO employeeTasks (userID, taskID, hours)
VALUES (3, 1, 20), -- assign 20 hours for user with userID 3 to task with taskID 1
       (3, 2, 15); -- assign 15 hours for user with userID 3 to task with taskID 2

