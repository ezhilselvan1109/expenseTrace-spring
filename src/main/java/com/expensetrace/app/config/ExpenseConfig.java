package com.expensetrace.app.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpenseConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
