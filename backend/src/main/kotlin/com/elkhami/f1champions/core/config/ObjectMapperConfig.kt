package com.elkhami.f1champions.core.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfig {
    @Bean
    fun appObjectMapper(): ObjectMapper {
        return ObjectMapper()
    }
}
