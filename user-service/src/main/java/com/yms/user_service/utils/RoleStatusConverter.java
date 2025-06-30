package com.yms.user_service.utils;

import com.yms.user_service.enums.RoleStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleStatusConverter implements AttributeConverter<RoleStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(RoleStatus status) {
        return status != null ? status.getCode() : null;
    }

    @Override
    public RoleStatus convertToEntityAttribute(Integer dbData) {
        return dbData != null ? RoleStatus.fromCode(dbData) : null;
    }
}
