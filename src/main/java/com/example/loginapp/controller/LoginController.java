package com.example.loginapp.controller;

import com.example.loginapp.model.User;
import com.example.loginapp.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // SINGLE constructor - Spring will inject both dependencies
    public LoginController(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {

        // Password validation
        if (password.length() < 6 || !password.matches(".*\\d.*")) {
            model.addAttribute("message", "Password must be at least 6 characters and contain at least 1 number.");
            return "login";
        }

        // Hash the password before saving
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(username, encodedPassword);
        userRepository.save(user);

        model.addAttribute("message", "User saved successfully");
        return "result";
    }

}

