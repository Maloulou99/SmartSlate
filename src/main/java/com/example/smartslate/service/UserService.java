package com.example.smartslate.service;

import com.example.smartslate.model.User;
import com.example.smartslate.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public int createUser(User newUser){
        return userRepository.createUser(newUser);
    }

    public User getUser(int userId){
        return userRepository.getUser(userId);
    }

    public void updateUser(User updatedUser){
        userRepository.updateUser(updatedUser);
    }

    public void deleteUser(int userId){
        userRepository.deleteUser(userId);
    }
    public List<User> getAllUsers(){
       return userRepository.getAllUsers();
    }

    public List<User> getProjectManagersByProjectId(int projectId){
        return userRepository.getProjectManagersByProjectId(projectId);
    }

    public User getUserByUsername(String username){
        return userRepository.getUserByUsername(username);
    }
}
