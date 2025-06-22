package com.yms.auth_service.utils;

import com.yms.auth_service.enums.UserStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserStatusConverter implements AttributeConverter<UserStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserStatus status) {
        return status != null ? status.getCode() : null;
    }

    @Override
    public UserStatus convertToEntityAttribute(Integer dbData) {
        return dbData != null ? UserStatus.fromCode(dbData) : null;
    }
}
