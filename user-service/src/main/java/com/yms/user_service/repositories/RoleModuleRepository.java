package com.yms.user_service.repositories;

import com.yms.user_service.entities.RoleModule;
import com.yms.user_service.enums.RoleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleModuleRepository extends JpaRepository<RoleModule, UUID> {

    boolean existsByName(String name);

    List<RoleModule> findByNameInAndStatus(List<String> moduleNames, RoleStatus roleStatus);
}
