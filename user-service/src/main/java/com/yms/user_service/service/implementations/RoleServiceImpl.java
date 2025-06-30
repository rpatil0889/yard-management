package com.yms.user_service.service.implementations;

import com.yms.user_service.dto.request.CreateRoleRequest;
import com.yms.user_service.dto.request.UpdateRolePermissionRequest;
import com.yms.user_service.dto.response.ApiResponse;
import com.yms.user_service.dto.response.RoleResponse;
import com.yms.user_service.entities.Permission;
import com.yms.user_service.entities.Role;
import com.yms.user_service.entities.RoleModule;
import com.yms.user_service.entities.RoleModulePermission;
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
        if (optionalRole.isEmpty()){
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Role not found", null);
        }
        Role role = optionalRole.get();
        Map<PermissionType, Permission> permissionsMap = permissionRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Permission::getName, p -> p));

        List<String> moduleNames = new ArrayList<>(request.getModulePermissions().keySet());

        List<RoleModule> modules= roleModuleRepository.findByNameInAndStatus(moduleNames, RoleStatus.ACTIVE);

        List<RoleModulePermission> permissions = roleModulePermissionRepository.findByRole(role);

        permissions.clear();

        for (String moduleName : request.getModulePermissions().keySet()) {
            RoleModule module = modules.stream()
                    .filter(m -> m.getName().equals(moduleName))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Module not found: " + moduleName));

            for (PermissionType permissionName : request.getModulePermissions().get(moduleName)) {
                Permission permission = permissionsMap.get(permissionName);
                if (permission == null) {
                    return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Permission not found: " + permissionName, null);
                }
                RoleModulePermission roleModulePermission = new RoleModulePermission();
                roleModulePermission.setRole(role);
                roleModulePermission.setModule(module);
                roleModulePermission.setPermission(permission);
                permissions.add(roleModulePermission);
            }
        }
        roleModulePermissionRepository.saveAll(permissions);
        return new ApiResponse<>(HttpStatus.OK.value(), "Role permissions updated successfully", null);

    }
}
