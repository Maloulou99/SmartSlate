package com.example.smartslate.repository;

import com.example.smartslate.model.User;

import java.util.List;

public interface IUserRepository {

    int createUser(User newUser);

    User getUser(int userId);

    void updateUser(User updatedUser);

    void deleteUser(int userId);

    List<User> getAllUsers();

    List<User> getProjectManagersByRoleId();

    String getRoleName(int roleID);

    User getProjectManagerById(int projectManagerId);

    User getProjectManagerByProjectId(int projectId);

    int getUserIdByProjectId(int projectId);

    User getUserFullNames(int userId);

    User getProjectManagersFullNames(int userId);
    List<User> getEmployeesByRoleId();
    User getEmployeesFullNames(int userId);
    List<User> getEmployeesByProjectId(int projectId);
}
