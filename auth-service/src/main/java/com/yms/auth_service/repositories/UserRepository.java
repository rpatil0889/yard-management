package com.yms.auth_service.repositories;

import com.yms.auth_service.entities.User;
import com.yms.auth_service.enums.UserStatus;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * UserRepository is a JPA repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Checks if a user with the given email exists.
     *
     * @param email The email address to search for.
     * @return true if a user with the given email address exists, false otherwise.
     */
    boolean existsByEmail(@Email(message = "Invalid email address") String email);


    Optional<User> findByEmailAndStatus(String email, UserStatus userStatus);
}
