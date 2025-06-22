package com.yms.auth_service.repositories;

import com.yms.auth_service.entities.Role;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository is a JPA Repository for the Role entity.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    /**
     * Retrieve a role by its name.
     *
     * @param name The name of the role to be retrieved.
     * @return An Optional containing the role if found, or an empty Optional if not found.
     */
    Optional<Role> findByName(String name);

    /**
     * Retrieve a list of roles by their names.
     *
     * @param role The list of role names to be retrieved. Must not be empty.
     * @return A list of roles matching the provided names.
     */
    List<Role> findByNameIn(@NotEmpty(message = "Role is required") List<String> role);
}
