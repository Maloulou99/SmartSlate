package com.example.smartslate.service;

import com.example.smartslate.model.Project;
import com.example.smartslate.repository.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;



    public int createProject(Project project){
        return projectRepository.createProject(project);

    }

    public void updateProject(Project project){
        projectRepository.updateProject(project);
    }
    public void deleteProject (int projectId){
        projectRepository.deleteProject(projectId);
    }




}
