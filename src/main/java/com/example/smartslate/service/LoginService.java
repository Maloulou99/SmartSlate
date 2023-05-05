package com.example.smartslate.service;

import com.example.smartslate.model.User;
import com.example.smartslate.repository.LoginRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private LoginRepository loginRepository;

    public User checkUser(String email, String password) {
        return loginRepository.checkUser(email, password);
    }

    public User findByEmailAndPassword(String email, String password){
        return loginRepository.findByEmailAndPassword(email, password);
    }
    public User findByUsernameAndPassword(String username, String password){
        return loginRepository.findByUsernameAndPassword(username, password);
    }

    public boolean isLoggedIn(HttpSession session, int userId){
        return loginRepository.isLoggedIn(session, userId);
    }



}

