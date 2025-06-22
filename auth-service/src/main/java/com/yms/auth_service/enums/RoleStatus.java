package com.yms.auth_service.enums;

import java.util.Arrays;

public enum RoleStatus {
    ACTIVE(1),
    INACTIVE(2),
    DELETED(3);

    private final int code;

    RoleStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static RoleStatus fromCode(int code) {
        return Arrays.stream(values())
                .filter(status -> status.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid RoleStatus code: " + code));
    }
}
