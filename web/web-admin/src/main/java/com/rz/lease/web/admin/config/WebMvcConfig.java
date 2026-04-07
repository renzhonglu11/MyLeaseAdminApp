package com.rz.lease.web.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final StringToBaseEnumConverterFactory stringToBaseEnumConverterFactory;

    public WebMvcConfig(StringToBaseEnumConverterFactory stringToBaseEnumConverterFactory) {
        this.stringToBaseEnumConverterFactory = stringToBaseEnumConverterFactory;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(stringToBaseEnumConverterFactory);
    }

}
