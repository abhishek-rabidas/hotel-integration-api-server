package com.github.abhishek_rabidas.Hotel_Integration_API.controller;

import com.github.abhishek_rabidas.Hotel_Integration_API.dto.UserSignupRequest;
import com.github.abhishek_rabidas.Hotel_Integration_API.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public void signup(@RequestBody UserSignupRequest request) {
        userService.signUp(request);
    }

    @PostMapping("/login")
    public void login() {

    }
}
