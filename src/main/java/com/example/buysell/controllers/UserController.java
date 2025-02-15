package com.example.buysell.controllers;


import com.example.buysell.models.User;
import com.example.buysell.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @PostMapping("/registration")
    public String createUser(User user, Model model){
        if(!userService.addUser(user)){
            model.addAttribute("errorMessage", "пользователь с email: " + user.getEmail() + "уже существует");
            return "registration";
        }
        return "redirect:/login";
    }

}
