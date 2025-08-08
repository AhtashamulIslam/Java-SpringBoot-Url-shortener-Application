package com.url.shortener.repository;

import com.url.shortener.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Here we declare our methods which are not available in jpa library ,and it will create query language for the
// particular methods.
@Repository
public interface UserRepository extends JpaRepository<User ,Long> {

    Optional<User> findByUsername(String username);

}
