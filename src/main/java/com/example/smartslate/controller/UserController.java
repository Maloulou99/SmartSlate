package com.example.smartslate.controller;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.Task;
import com.example.smartslate.model.User;
import com.example.smartslate.repository.ILoginRepository;
import com.example.smartslate.repository.IProjectRepository;
import com.example.smartslate.repository.ITaskRepository;
import com.example.smartslate.repository.IUserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/smartslate")
@Controller
public class UserController {
    private IUserRepository iUserRepository;
    private IProjectRepository iProjectRepository;
    private ITaskRepository iTaskRepository;
    private ILoginRepository iLoginRepository;

    public UserController(IUserRepository iUserRepository, IProjectRepository iProjectRepository, ITaskRepository iTaskRepository, ILoginRepository iLoginRepository) {
        this.iUserRepository = iUserRepository;
        this.iProjectRepository = iProjectRepository;
        this.iTaskRepository = iTaskRepository;
        this.iLoginRepository = iLoginRepository;
    }

    @GetMapping("/create/user")
    public String createUser(Model model) {
        User newUser = new User();
        model.addAttribute("newUser", newUser);
        return "create-user";
    }

    @GetMapping("/user/{userId}")
    public String getUser(@PathVariable int userId, Model model) {
        User user = iUserRepository.getUser(userId);
        String roleName = iUserRepository.getRoleName(user.getRoleID());
        List<Project> projects = iProjectRepository.getAllProjectsByUserId(userId);
        List<Task> tasks = iTaskRepository.getTasksByProjectManagerID(user.getUserID());

        user.setRoleName(roleName);
        model.addAttribute("user", user);
        model.addAttribute("roleName", roleName);
        model.addAttribute("projects", projects);
        model.addAttribute("tasks", tasks);

        return "pm-frontpage";
    }




    @PostMapping("/adduser")
    public String addUser(@ModelAttribute User newUser, Model model) {
        int userId = iUserRepository.createUser(newUser);
        newUser.setUserID(userId);
        model.addAttribute("user", newUser);
        model.addAttribute("createdAt", newUser.getCreatedAt());
        model.addAttribute("username", newUser.getUsername());
        model.addAttribute("firstName", newUser.getFirstName());
        model.addAttribute("lastName", newUser.getLastName());
        model.addAttribute("email", newUser.getEmail());
        model.addAttribute("password", newUser.getPassword());
        model.addAttribute("phoneNumber", newUser.getPhoneNumber());
        model.addAttribute("role", iUserRepository.getRoleName(newUser.getRoleID())); // get role name instead of role id
        return "user-created";
    }

    @GetMapping("/user/update/{userId}")
    public String updateUserForm(@PathVariable int userId, Model model, HttpSession httpSession) {
        Integer loggedUserId = (Integer) httpSession.getAttribute("userId");
        if (loggedUserId == null || loggedUserId != userId) {
            return "redirect:/login";
        }

        User user = iUserRepository.getUser(userId);
        model.addAttribute("user", user);
        return "user-update";
    }

    @PostMapping("/user/update")
    public String updateUser(@ModelAttribute User user, Model model) {
        iUserRepository.updateUser(user);
        iProjectRepository.getProjectById(user.getUserID());
        model.addAttribute("id", user.getUserID());
        return "redirect:/smartslate/user/" + user.getUserID();
    }


    @GetMapping("/deleteuser/{userId}")
    public String deleteUser(@PathVariable int userId) {
        iUserRepository.deleteUser(userId);
        return "redirect:/";
    }

}









