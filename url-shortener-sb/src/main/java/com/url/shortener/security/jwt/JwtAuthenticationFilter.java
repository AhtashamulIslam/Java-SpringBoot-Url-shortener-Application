package com.url.shortener.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// This method is invoked for every request only once.
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;
    // This is an interface from spring security. We use it to load the user details to use as DTO.
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Here we add method to authenticate the request.
        try{
             // Get Jwt from header
              String jwt = jwtTokenProvider.getJwtFromHeader(request);
             // validate token.
             // If token is valid, get user details ( From helper methods from JwtUtils )

            if(jwt != null && jwtTokenProvider.verifyToken(jwt)){
                String username = jwtTokenProvider.getUserNameFromJwtToken(jwt);
                //get username -> load user -> set the auth context which we already used in JwtUtils.
                // These services come from spring security.
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // We can also implement our own user details service by overriding the default spring user
                // detail service. This data will be loaded from UserDetailsServiceImpl service method by username.

              // We make an authentication object with this user details which we will use to check validation of user.
              if(userDetails != null){
                  UsernamePasswordAuthenticationToken authentication =
                          new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
              // We set details based on the request.
                  authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              // Now we make a security context based on this authentication object
                  SecurityContextHolder.getContext().setAuthentication(authentication);
              }
            }
        }catch(Exception e){
          e.printStackTrace();
        }
        // To continue the filter chain we have to do this.
        filterChain.doFilter(request,response);
    }
}






