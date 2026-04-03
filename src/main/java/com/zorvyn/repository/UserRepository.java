package com.zorvyn.repository;

import com.zorvyn.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    //for login
    Optional<User> findByEmail(String email);
    //to check if user exists when registering
    Boolean existsByEmail(String email);
}
