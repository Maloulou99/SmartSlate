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
}
