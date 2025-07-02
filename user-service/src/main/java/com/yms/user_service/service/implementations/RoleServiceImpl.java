package com.yms.user_service.service.implementations;

import com.yms.user_service.dto.request.CreateRoleRequest;
import com.yms.user_service.dto.request.RolePermissionDTO;
import com.yms.user_service.dto.request.UpdateRolePermissionRequest;
import com.yms.user_service.dto.response.ApiResponse;
import com.yms.user_service.dto.response.RoleResponse;
import com.yms.user_service.entities.Permission;
import com.yms.user_service.entities.Role;
import com.yms.user_service.entities.RoleModule;
import com.yms.user_service.entities.RoleModulePermission;
import com.yms.user_service.enums.ModuleStatus;
import com.yms.user_service.enums.PermissionType;
import com.yms.user_service.enums.RoleStatus;
import com.yms.user_service.repositories.PermissionRepository;
import com.yms.user_service.repositories.RoleModulePermissionRepository;
import com.yms.user_service.repositories.RoleModuleRepository;
import com.yms.user_service.repositories.RoleRepository;
import com.yms.user_service.service.interfaces.RoleService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final RoleModuleRepository roleModuleRepository;

    private final RoleModulePermissionRepository roleModulePermissionRepository;

    private final PermissionRepository permissionRepository;

    private final ModelMapper modelMapper;


    @Override
    public ApiResponse<String> createRole(CreateRoleRequest createRoleRequest) {

        Optional<Role> optionalRole = roleRepository.findByName(createRoleRequest.getName());
        if (optionalRole.isPresent()) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Role already exists", null);
        }
        Role role = new Role();
        role.setName(createRoleRequest.getName());
        role.setDescription(createRoleRequest.getDescription());
        role.setStatus(RoleStatus.ACTIVE);
        role.setCreatedOn(LocalDateTime.now());
        roleRepository.save(role);
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Role created successfully", null);
    }

    @Override
    public ApiResponse<String> updateRole(CreateRoleRequest createRoleRequest) {

        if (createRoleRequest.getId() == null) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Role id is required", null);
        }
        Role role = roleRepository.findById(createRoleRequest.getId())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        role.setName(createRoleRequest.getName());
        role.setDescription(createRoleRequest.getDescription());
        role.setStatus(RoleStatus.valueOf(createRoleRequest.getStatus()));
        role.setUpdatedOn(LocalDateTime.now());
        roleRepository.save(role);
        return new ApiResponse<>(HttpStatus.OK.value(), "Role updated successfully", null);
    }

    @Override
    public ApiResponse<String> deleteRole(UUID id) {
        roleRepository.deleteById(id);
        return new ApiResponse<>(HttpStatus.OK.value(), "Role deleted successfully", null);
    }

    @Override
    public ApiResponse<RoleResponse> getRole(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        RoleResponse response = modelMapper.map(role, RoleResponse.class);
        return new ApiResponse<>(HttpStatus.OK.value(), "Role found successfully", response);
    }

    @Override
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        List<RoleResponse> responseList = roles.stream().map(role -> modelMapper.map(role, RoleResponse.class)).toList();
        return new ApiResponse<>(HttpStatus.OK.value(), "Roles found successfully",responseList);
    }

    @Override
    public ApiResponse<String> updateRolePermissions(UpdateRolePermissionRequest request) {

        Optional<Role> optionalRole = roleRepository.findById(request.getRoleId());
        if (optionalRole.isEmpty()) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Role not found", null);
        }
        Role role = optionalRole.get();

        // Clear old permissions
        List<RoleModulePermission> existingPermissions = roleModulePermissionRepository.findByRole(role);
        if (existingPermissions != null && !existingPermissions.isEmpty()) {
            roleModulePermissionRepository.deleteAll(existingPermissions);
        }

        if(CollectionUtils.isEmpty(request.getModulePermissions())) {
            return new ApiResponse<>(HttpStatus.OK.value(), "Role permission updated successfully", null);
        }

        // Load and validate permissions
        List<Permission> allPermissions = permissionRepository.findAll();
        if (allPermissions == null || allPermissions.isEmpty()) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "No permissions found in system", null);
        }

        Map<PermissionType, Permission> permissionsMap = allPermissions.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Permission::getName, p -> p));

        // Extract module names safely
        List<String> moduleNames = request.getModulePermissions().stream()
                .filter(Objects::nonNull)
                .map(RolePermissionDTO::getModuleName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (moduleNames.isEmpty()) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "No valid module names found", null);
        }

        // Fetch valid modules
        List<RoleModule> modules = roleModuleRepository.findByNameInAndStatus(moduleNames, ModuleStatus.ACTIVE);
        if (modules == null || modules.isEmpty()) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "No valid active modules found", null);
        }



        List<RoleModulePermission> newPermissions = new ArrayList<>();

        for (RolePermissionDTO dto : request.getModulePermissions()) {
            if (dto == null || dto.getModuleName() == null || dto.getPermissions() == null) continue;

            String moduleName = dto.getModuleName();
            RoleModule module = modules.stream()
                    .filter(m -> m.getName().equalsIgnoreCase(moduleName))
                    .findFirst()
                    .orElse(null);

            if (module == null) {
                return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Module not found or inactive: " + moduleName, null);
            }

            for (String permString : dto.getPermissions()) {
                if (permString == null || permString.isBlank()) continue;

                PermissionType permissionType;
                try {
                    permissionType = PermissionType.valueOf(permString.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Invalid permission: " + permString, null);
                }

                Permission permission = permissionsMap.get(permissionType);
                if (permission == null) {
                    return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Permission not found in system: " + permissionType, null);
                }

                RoleModulePermission roleModulePermission = new RoleModulePermission();
                roleModulePermission.setRole(role);
                roleModulePermission.setModule(module);
                roleModulePermission.setPermission(permission);
                newPermissions.add(roleModulePermission);
            }
        }

        if (!newPermissions.isEmpty()) {
            roleModulePermissionRepository.saveAll(newPermissions);
        }

        return new ApiResponse<>(HttpStatus.OK.value(), "Role permissions updated successfully", null);
    }


}
