package com.yms.user_service.dto.request;

import com.yms.user_service.enums.PermissionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRolePermissionRequest {

    private UUID roleId;

    private Map<String,List<PermissionType>> modulePermissions;


}
