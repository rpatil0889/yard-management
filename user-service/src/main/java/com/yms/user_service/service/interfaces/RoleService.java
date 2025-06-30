package com.yms.user_service.service.interfaces;

import com.yms.user_service.dto.request.CreateRoleRequest;
import com.yms.user_service.dto.request.UpdateRolePermissionRequest;
import com.yms.user_service.dto.response.ApiResponse;
import com.yms.user_service.dto.response.RoleResponse;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    ApiResponse<String>createRole(CreateRoleRequest createRoleRequest);

    ApiResponse<String>updateRole(CreateRoleRequest createRoleRequest);

    ApiResponse<String>deleteRole(UUID id);

    ApiResponse<RoleResponse>getRole(UUID id);

    ApiResponse<List<RoleResponse>>getAllRoles();

    ApiResponse<String> updateRolePermissions(UpdateRolePermissionRequest request);
}
