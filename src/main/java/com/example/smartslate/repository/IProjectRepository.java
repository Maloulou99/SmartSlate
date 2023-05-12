package com.example.smartslate.repository;

import com.example.smartslate.model.Project;

import java.util.List;

public interface IProjectRepository {

    int createProject(Project project);
    void updateProject(Project project);
    void deleteProject(int projectId);
    Project getProjectById(int projectId);
    List<Project> getAllProjects();
    List<Project> getAllProjectsByUserId(int userId);
}
