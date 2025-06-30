package com.yms.user_service.utils;

import com.yms.user_service.enums.ModuleStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ModuleStatusConverter implements AttributeConverter<ModuleStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ModuleStatus status) {
        return status != null ? status.getCode() : null;
    }

    @Override
    public ModuleStatus convertToEntityAttribute(Integer dbData) {
        return dbData != null ? ModuleStatus.fromCode(dbData) : null;
    }
}
