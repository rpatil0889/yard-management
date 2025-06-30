package com.yms.user_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoleRequest {

    private UUID id;

    private String name;

    private String description;

    private String status;

}
