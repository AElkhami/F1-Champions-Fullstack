package com.elkhami.f1champions.core.resilience4j

import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RateLimiterRegistryConfig {
    @Bean
    fun rateLimiterRegistry(): RateLimiterRegistry = RateLimiterRegistry.ofDefaults()
}
