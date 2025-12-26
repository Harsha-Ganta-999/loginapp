package com.example.loginapp.controller;

import com.example.loginapp.model.User;
import com.example.loginapp.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute User user, Model model) {
        userRepository.save(user);
        model.addAttribute("message", "User saved successfully!");
        return "result";
    }
}
