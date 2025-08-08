package com.url.shortener.service;

import com.url.shortener.dtos.LoginRequest;
import com.url.shortener.models.User;
import com.url.shortener.repository.UserRepository;
import com.url.shortener.security.jwt.JwtAuthenticationResponse;
import com.url.shortener.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository; // We interact the DB through this repository instance.
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    public User registerUser(User user){
         user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // We use JwtAuthenticationResponse which will give us a valid token after authentication.
    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest){
       // We pass username,password through the controller and here we will be sent an authenticated
        // token for a valid user.
        Authentication authentication = authenticationManager.authenticate(
                 // This authenticationManager is made bean in security config.
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword())
                // This class represent the credentials of user.
        );
        // For a valid user, a fully populated authentication object.
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // We hold the authentication object for current request and session in our context by spring
        // security construct.

        // Now we generate a token using user details and send it as a response of JwtAuthenticationResponse
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);
        return new JwtAuthenticationResponse(jwt); // This will return a token as a response.
    }

    public User findByUsername(String name) {
      return userRepository.findByUsername(name).orElseThrow(
              () -> new UsernameNotFoundException("User not found with that username: "+name)
      );
    }
}




