package com.url.shortener.security.jwt;


import lombok.AllArgsConstructor;
import lombok.Data;

// This will give us response from after jwt authentication ,and it's a DTO class.
@Data
@AllArgsConstructor
public class JwtAuthenticationResponse {
    private String token;
}
