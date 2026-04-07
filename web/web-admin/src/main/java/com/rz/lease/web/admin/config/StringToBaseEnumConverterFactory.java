package com.rz.lease.web.admin.config;

import com.rz.lease.model.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

@Component
public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {

    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToBaseEnumConverter<>(targetType);
    }

    private static class StringToBaseEnumConverter<T extends BaseEnum> implements Converter<String, T> {

        private final Class<T> targetType;

        private StringToBaseEnumConverter(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        public T convert(String source) {
            if (source == null || source.isBlank()) {
                return null;
            }

            T[] enumConstants = targetType.getEnumConstants();
            if (enumConstants == null) {
                throw new IllegalArgumentException(targetType.getName() + " is not an enum type");
            }

            for (T value : enumConstants) {
                Enum<?> enumValue = (Enum<?>) value;
                if (enumValue.name().equalsIgnoreCase(source)) {
                    return value;
                }
                if (String.valueOf(value.getCode()).equals(source)) {
                    return value;
                }
            }

            throw new IllegalArgumentException("Unknown " + targetType.getSimpleName() + ": " + source);
        }
    }
}
