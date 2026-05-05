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

    public LoginController(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Show register page
    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    // Process registration
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {

        // Check if username already exists
        if (userRepository.findByUsername(username) != null) {
            model.addAttribute("error", "Username already taken!");
            return "register";
        }

        // Validate password
        if (password.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters.");
            return "register";
        }

        // Hash and save
        String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User(username, hashedPassword);
        userRepository.save(newUser);

        model.addAttribute("success", "Account created! Please sign in.");
        return "login";
    }

    // Show login page
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // Process login
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {

        User existingUser = userRepository.findByUsername(username);

        if (existingUser == null) {
            model.addAttribute("error", "User not found. Please register first.");
            return "login";
        }

        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
            model.addAttribute("error", "Invalid password.");
            return "login";
        }

        return "redirect:/home?username=" + username;
    }

    // Show forgot password page
    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    // Process password reset
    @PostMapping("/forgot-password")
    public String resetPassword(@RequestParam String username,
                                @RequestParam String newPassword,
                                Model model) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            model.addAttribute("error", "Username not found.");
            return "forgot-password";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        model.addAttribute("success", "Password updated! Please login.");
        return "login";
    }
}