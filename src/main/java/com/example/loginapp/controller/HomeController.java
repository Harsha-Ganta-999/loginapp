package com.example.loginapp.controller;

import com.example.loginapp.model.Suggestion;
import com.example.loginapp.repository.SuggestionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final SuggestionRepository suggestionRepository;

    public HomeController(SuggestionRepository suggestionRepository) {
        this.suggestionRepository = suggestionRepository;
    }

    @GetMapping("/home")
    public String home(@RequestParam(required = false, defaultValue = "User") String username,
                       Model model) {
        model.addAttribute("username", username);
        return "home";
    }

    @PostMapping("/home")
    public String submitSuggestion(
            @RequestParam(required = false, defaultValue = "User") String username,
            @RequestParam String message,
            Model model) {

        Suggestion suggestion = new Suggestion(username, message);
        suggestionRepository.save(suggestion);

        model.addAttribute("username", username);
        model.addAttribute("success", "Thank you! Your suggestion was submitted.");
        return "home";
    }
}