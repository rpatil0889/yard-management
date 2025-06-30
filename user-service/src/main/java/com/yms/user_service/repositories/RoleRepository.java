package com.yms.user_service.repositories;

import com.yms.user_service.entities.Role;
import com.yms.user_service.enums.RoleStatus;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(@NotBlank(message = "Name is required") String name);

    Set<Role> findByNameInAndStatus(List<String> names, RoleStatus status);
}
