package com.yms.user_service.dto.response;

import com.yms.user_service.enums.RoleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private String name;

    private String description;

    private String status;

    private List<ModuleResponse> modules;

}
