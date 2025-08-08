package com.url.shortener.repository;


import com.url.shortener.models.UrlMapping;
import com.url.shortener.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping ,Long> {
    // Defining custom method to find short url object using shortUrl.
    UrlMapping findByShortUrl(String shortUrl);

    // All the urls of a particular user.
    List<UrlMapping> findByUser(User user);
}
