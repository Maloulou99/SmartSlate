INSERT INTO roles (roleName)
VALUES ('Employee');

INSERT INTO roles (roleName)
VALUES ('Customer');

INSERT INTO users (username, password, email, firstName, lastName, phoneNumber, roleID)
VALUES ('maloulou99', '1234', 'malou@email.com', 'Malou', 'Lundstrøm', '12345678', 1);

INSERT INTO users (username, password, email, firstName, lastName, phoneNumber, roleID)
VALUES ('heval1234', '1234', 'heval@email.com', 'Heval', 'Tirpan', '87654321', 1);

INSERT INTO users (username, password, email, firstName, lastName, phoneNumber, roleID)
VALUES ('bigduke', '1234', 'magnus@email.com', 'Magnus', 'Korse', '11223344', 2);

INSERT INTO projects (projectManagerID, projectName, description, startDate, endDate, budget, status)
VALUES (1, 'Project X', 'Description of project X', '2023-01-01', '2023-12-31', 100000.00, 'In progress');

INSERT INTO projects (projectManagerID, projectName, description, startDate, endDate, budget, status)
VALUES (1, 'Project Y', 'Description of project Y', '2023-06-01', '2023-09-30', 50000.00, 'In progress');

INSERT INTO projects (projectManagerID, projectName, description, startDate, endDate, budget, status)
VALUES (2, 'Project Y', 'Description of project Y', '2023-06-01', '2023-09-30', 50000.00, 'In progress');

INSERT INTO projects (projectManagerID, projectName, description, startDate, endDate, budget, status)
VALUES (3, 'Project Y', 'Description of project Y', '2023-06-01', '2023-09-30', 50000.00, 'In progress');

INSERT INTO tasks (projectID, description, deadline, assignedTo, status)
VALUES (1, 'Task 2 for project X', '2023-03-31', 1, 'Not started');

INSERT INTO tasks (projectID, description, deadline, assignedTo, status)
VALUES (2, 'Task 1 for project X', '2023-02-28', 2, 'In progress');

INSERT INTO tasks (projectID, description, deadline, assignedTo, status)
VALUES (3, 'Task 2 for project X', '2023-03-31', 3, 'Not started');


INSERT INTO employeeTasks (taskEmployeeID, taskID, hours)
VALUES (1, 100, 20.5);

INSERT INTO employeeTasks (taskEmployeeID, taskID, hours)
VALUES (2, 101, 15.0);

INSERT INTO employeeTasks (taskEmployeeID, taskID, hours)
VALUES (3, 102, 10.0);
