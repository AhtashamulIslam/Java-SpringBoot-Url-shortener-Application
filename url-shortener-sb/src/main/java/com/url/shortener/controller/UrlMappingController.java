package com.url.shortener.controller;


import com.url.shortener.dtos.ClickEventDTO;
import com.url.shortener.dtos.UrlMappingDTO;
import com.url.shortener.models.ClickEvent;
import com.url.shortener.models.User;
import com.url.shortener.service.UrlMappingService;
import com.url.shortener.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
@AllArgsConstructor
public class UrlMappingController {
    private UrlMappingService urlMappingService;
    private UserService userService;
    // Api to shorten url
    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')") // Only authenticated user can do this task.
    public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody Map<String, String> request,
                                                        Principal principal){
                          // Principal from spring security to give an authenticated user from security context.

           // Here we have to pass a map type of request body.
       // { "originalUrl": "http://...." } format will be key value pair.
        String originalUrl = request.get("originalUrl");
        User user = userService.findByUsername(principal.getName());
        // Here we use the service level method of urlMappingService.
        UrlMappingDTO urlMappingDTO = urlMappingService.createShortUrl(originalUrl, user);
        //Finally we make a short url and pass this as response DTO to the DB.
        return ResponseEntity.ok(urlMappingDTO);
    }

    // API to find all the urls for a particular user.
    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingDTO>> getUserUrls(Principal principal){
        User user = userService.findByUsername(principal.getName());
        // We will find the urls of a particular user.
        List<UrlMappingDTO> urls = urlMappingService.getUrlsByUser(user);
        return ResponseEntity.ok(urls);
    }

    // API to get the date and clicks of a particular url.
    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClickEventDTO>> getUrlAnalytics(@PathVariable String shortUrl,
                                                               @RequestParam("startDate") String startDate,
                                                               @RequestParam("endDate") String endDate ){

        // Now we will be sent the clickDate and count from ClickEventDTO by shortUrl request param.

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // We set this as request param.
        LocalDateTime start = LocalDateTime.parse(startDate,formatter);
        LocalDateTime end = LocalDateTime.parse(endDate,formatter);
        List<ClickEventDTO> clickEventDTOS = urlMappingService.getClickEventsByDate(shortUrl , start, end);
        return ResponseEntity.ok(clickEventDTOS);
    }

    // API to get the total clicks of a user in a particular day for all urls.

    @GetMapping("/totalClicks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<LocalDate, Long>> getTotalClicksByDate(Principal principal,
                                                                     @RequestParam("startDate") String startDate,
                                                                     @RequestParam("endDate") String endDate ){
        // As we will be fetched one instance with a date property.So we can use Map here.
         // We get the data grouped by value of date : click counts
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE; // We set this as request param.
        User user = userService.findByUsername(principal.getName());
        LocalDate start = LocalDate.parse(startDate,formatter);
        LocalDate end = LocalDate.parse(endDate,formatter);
        Map<LocalDate ,Long> totalClicks = urlMappingService.getTotalClicksByUserAndDate(user , start, end);
        // Based on the user we calculate the total clicks of all the urls, he shortens.
        return ResponseEntity.ok(totalClicks);

    }

}













