package com.yms.auth_service.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the application.
 */
@Configuration
public class ApplicationConfig {

    /**
     * A ModelMapper instance. This can be injected into any class and used to convert between different objects.
     * @return a ModelMapper instance
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
