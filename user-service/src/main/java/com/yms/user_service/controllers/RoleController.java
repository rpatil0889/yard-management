package com.yms.user_service.controllers;

import com.yms.user_service.dto.request.CreateRoleRequest;
import com.yms.user_service.dto.request.UpdateRolePermissionRequest;
import com.yms.user_service.dto.response.ApiResponse;
import com.yms.user_service.dto.response.RoleResponse;
import com.yms.user_service.service.interfaces.RoleService;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@GraphQLApi
@Service
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleService roleService;

    @GraphQLMutation(name = "createRole", description = "Create a new role")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> createRole(@GraphQLArgument(name = "roleRequest") CreateRoleRequest createRoleRequest) {
        try {
            return roleService.createRole(createRoleRequest);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    @GraphQLMutation(name = "updateRole", description = "Update an existing role")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> updateRole(@GraphQLArgument(name = "roleRequest") CreateRoleRequest createRoleRequest) {
        try {
            return roleService.updateRole(createRoleRequest);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    @GraphQLMutation(name = "deleteRole", description = "Delete a role by ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteRole(@GraphQLArgument(name = "id") UUID id) {
        try {
            return roleService.deleteRole(id);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    @GraphQLQuery(name = "getRole", description = "Get a role by ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<RoleResponse> getRole(@GraphQLArgument(name = "id") UUID id) {
        try {
            return roleService.getRole(id);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    @GraphQLQuery(name = "listRoles", description = "List all roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<RoleResponse>> listRoles() {
        try {
            return roleService.getAllRoles();
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    @GraphQLMutation(name="updatePermissions", description = "Update permissions for a role")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> updateRolePermissions(@GraphQLArgument(name = "request") UpdateRolePermissionRequest request) {
        try {
            return roleService.updateRolePermissions(request);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }
}
