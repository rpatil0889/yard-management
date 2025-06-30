package com.yms.user_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String name;

    // Additional fields can be added as needed
}
