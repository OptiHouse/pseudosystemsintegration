package com.pseudoorganization.pseudosystemsintegration.repository;

import com.pseudoorganization.pseudosystemsintegration.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserById(Long userID);
    Optional<User> findUserByUsername(String username);
}
