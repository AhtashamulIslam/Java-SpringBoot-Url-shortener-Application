package com.url.shortener.security.jwt;

import com.url.shortener.service.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import java.util.stream.Collectors;

@Component
public class JwtUtils {
    // Here we extract the token from authorized user header and send it for every request. Based on that we send
    // success or error.
    // Authorization -> Bearer <TOKEN>

    @Value("${jwt.secret}") // These values come from application.properties.
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;
   public String getJwtFromHeader(HttpServletRequest request) {
       String bearerToken = request.getHeader("Authorization");
       if (bearerToken != null && bearerToken.startsWith("Bearer")) {
           return bearerToken.substring(7);
       }
       return null;
   }

   // Now we generate a token for new authorized user.
    public String generateToken(UserDetailsImpl userDetails){

          String username = userDetails.getUsername();
          String roles = userDetails.getAuthorities().stream()
                  .map(authority -> authority.getAuthority())
                  .collect(Collectors.joining(","));
            // As a user can have multiple roles as well.
        return Jwts.builder()
                .subject(username)
                .claim("roles",roles)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime() + jwtExpirationMs)))
                .signWith(key())
                .compact();
    }

    // Parse the token to extract the username
    public String getUserNameFromJwtToken(String token){
       return Jwts.parser()
               .verifyWith((SecretKey) key()) // Here we verify the user
               .build().parseSignedClaims(token) //Here we parse the token
               .getPayload().getSubject(); // As we use username as subject, we have to get payload first
                                          // after that we can get the username.
    }
    // Generate a key generator method from java security and pass this to signWith in jwt builder.
    private Key key(){
       return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Build a method to validate token when user is authenticated and try to make request.

    public boolean verifyToken(String authToken){
       try {
           Jwts.parser().verifyWith((SecretKey) key())
                   .build().parseSignedClaims(authToken);
           return true;
       }catch (JwtException e){
           throw new RuntimeException(e);
       }catch (IllegalArgumentException e){
           throw new RuntimeException(e);
       }catch (Exception e){
           throw new RuntimeException(e);
       }
   }

}













