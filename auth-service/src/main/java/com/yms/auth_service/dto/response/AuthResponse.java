package com.yms.auth_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * The Class AuthResponse.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private String name;
    private String email;
    private List<String>role;
    private String token;
    private List<String> authorities;


}
