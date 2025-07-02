package com.yms.auth_service.entities;

import com.yms.auth_service.enums.RoleStatus;
import com.yms.auth_service.utils.RoleStatusConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The Class Role.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class Role extends CommonFields{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "role_name", unique = true, nullable = false)
    private String name;

    @Column(name = "role_description")
    private String description;

    @Convert(converter = RoleStatusConverter.class)
    private RoleStatus status;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoleModulePermission> modulePermissions = new ArrayList<>();

}
