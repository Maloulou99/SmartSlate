package com.example.smartslate.controller;

import com.example.smartslate.repository.ILoginRepository;
import com.example.smartslate.repository.IProjectRepository;
import com.example.smartslate.repository.ITaskRepository;
import com.example.smartslate.repository.IUserRepository;

public class EmployeeController {
    private IUserRepository iUserRepository;
    private IProjectRepository iProjectRepository;
    private ITaskRepository iTaskRepository;
    private ILoginRepository iLoginRepository;

    public EmployeeController(IUserRepository iUserRepository, IProjectRepository iProjectRepository, ITaskRepository iTaskRepository, ILoginRepository iLoginRepository) {
        this.iUserRepository = iUserRepository;
        this.iProjectRepository = iProjectRepository;
        this.iTaskRepository = iTaskRepository;
        this.iLoginRepository = iLoginRepository;
    }

}
