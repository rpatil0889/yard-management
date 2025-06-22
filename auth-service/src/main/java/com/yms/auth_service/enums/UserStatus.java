package com.yms.auth_service.enums;

public enum UserStatus {
    ACTIVE(1),
    INACTIVE(2),
    SUSPENDED(3),
    DELETED(4);

    private final int code;

    UserStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static UserStatus fromCode(int code) {
        for (UserStatus status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid UserStatus code: " + code);
    }
}
