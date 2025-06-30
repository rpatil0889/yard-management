package com.yms.user_service.repositories;

import com.yms.user_service.entities.Role;
import com.yms.user_service.entities.RoleModulePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleModulePermissionRepository extends JpaRepository<RoleModulePermission, UUID> {

    List<RoleModulePermission> findByRole(Role role);
}
