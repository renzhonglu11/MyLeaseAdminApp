package com.rz.lease.model.enums.converter;

import com.rz.lease.model.enums.BaseEnum;
import jakarta.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Objects;

public abstract class BaseEnumConverter<E extends Enum<E> & BaseEnum>
        implements AttributeConverter<E, Integer> {

    private final Class<E> enumClass;

    protected BaseEnumConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    // Convert the enum to its corresponding code for database storage
    @Override
    public Integer convertToDatabaseColumn(E attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    // Convert the code from the database back to the enum
    @Override
    public E convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        return Arrays.stream(enumClass.getEnumConstants())
                .filter(value -> Objects.equals(value.getCode(), dbData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown " + enumClass.getSimpleName() + " code: " + dbData));
    }
}
