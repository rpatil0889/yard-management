package com.yms.user_service.enums;

import java.util.Arrays;

public enum ModuleStatus {
    ACTIVE(1),
    INACTIVE(2),
    ARCHIVED(3),
    DELETED(4);

    private final int code;

    ModuleStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ModuleStatus fromCode(int code) {
        return Arrays.stream(values())
                .filter(status -> status.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid RoleStatus code: " + code));
    }
}
