package com.url.shortener.controller;


import com.url.shortener.dtos.LoginRequest;
import com.url.shortener.dtos.RegisterRequest;
import com.url.shortener.models.User;
import com.url.shortener.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth") // We set initial end point for all api.
@AllArgsConstructor // To initialize services and other autowired objects.
public class AuthController {

    private UserService userService;

    // Login of a User
    @PostMapping("/public/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.authenticateUser(loginRequest));
    }
    // Register User
    @PostMapping("/public/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest){
                                      // Here we send a dto to DB through RegisterRequest class object.
         // And we will send the User object as request through this registerRequest.
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setRole("ROLE_USER"); // Any request is considered as role user.
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");

    }


}

















