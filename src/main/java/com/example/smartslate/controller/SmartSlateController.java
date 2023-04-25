package com.example.smartslate.controller;

import com.example.smartslate.model.User;
import com.example.smartslate.repository.SmartSlateRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


public class SmartSlateController {
    private SmartSlateRepository smartSlateRepository;

    public SmartSlateController(SmartSlateRepository smartSlateRepository){
        this.smartSlateRepository = smartSlateRepository;
    }

    @GetMapping("/create")
    public String createUser(Model model){
        User newUser = new User();
        model.addAttribute("newUser", newUser);
        return "";
    }

    @PostMapping("/adduser")
    public String addUser(@ModelAttribute User newUser, Model model){
        int userId = smartSlateRepository.createUser(newUser);
        model.addAttribute("firstName",newUser.getFirstName());
        model.addAttribute("lastName",newUser.getLastName());
        model.addAttribute("email",newUser.getEmail());
        model.addAttribute("password",newUser.getPassword());
        model.addAttribute("userId", userId);
        return "";
    }


}
