package com.example.CarRental.controller;

import com.example.CarRental.request.RegisterRequest;
import com.example.CarRental.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class AppController {

    private final UserService userService;

    @GetMapping("home")
    public String home(){
        return "index";
    }

    @GetMapping("signup")
    public  String getSignup(Model model){
        model.addAttribute("user", new RegisterRequest());
        return "signup";
    }

    @PostMapping("signup")
    public String signup(@ModelAttribute("user") RegisterRequest request){
        if (userService.signup(request))
            return "redirect:home?success";
        else
            return "redirect:home?error";
    }

    @GetMapping("login")
    public String getLogin(Model model) {
        return "login";
    }
}