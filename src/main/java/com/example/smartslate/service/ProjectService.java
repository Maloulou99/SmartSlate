package com.example.smartslate.service;

import com.example.smartslate.model.Project;
import com.example.smartslate.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public int createProject(Project project) {
        return projectRepository.createProject(project);
    }

    public Project getProjectById(int userId) {
        return projectRepository.getProjectById(userId);
    }

    public void updateProject(Project project) {
        projectRepository.updateProject(project);
    }

    public void deleteProject(int projectId) {
        projectRepository.deleteProject(projectId);
    }

    public List<Project> getAllProjects() {
       return projectRepository.getAllProjects();
    }

    public List<Project> getAllProjectsByUserId(int userId){
        return projectRepository.getAllProjectsByUserId(userId);
    }

    public void addUserToProject(int userId, int projectId) {
        projectRepository.addUserToProject(userId, projectId);
    }
}
