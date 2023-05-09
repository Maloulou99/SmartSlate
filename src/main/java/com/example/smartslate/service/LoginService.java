package com.example.smartslate.service;

import com.example.smartslate.model.User;
import com.example.smartslate.repository.LoginRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private LoginRepository loginRepository;

    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public User findByUsernameOrEmailAndPassword(String usernameOrEmail, String password){
        return loginRepository.findByUsernameOrEmailAndPassword(usernameOrEmail, password);
    }



}

