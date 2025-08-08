package com.url.shortener.controller;


import com.url.shortener.models.UrlMapping;
import com.url.shortener.service.UrlMappingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RedirectController {

    private UrlMappingService urlMappingService;
    // We use HTTP headers to map short url to original url.
    // After that, whenever anyone click the shortUrl , it will be redirected to original url.
    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirect(@PathVariable String shortUrl){
        UrlMapping urlMapping = urlMappingService.getOriginalUrl(shortUrl);
        if(urlMapping != null) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Location",urlMapping.getOriginalUrl());
               // We add a header with the original url so that anyone redirects to original url after clicking
               // short url.
            return ResponseEntity.status(302).headers(httpHeaders).build();
                // Here we build a http request with the original url.
        }else{
            return ResponseEntity.notFound().build();
        }
        }


}
