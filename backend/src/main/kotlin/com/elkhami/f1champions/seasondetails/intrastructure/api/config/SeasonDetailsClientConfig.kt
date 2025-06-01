package com.elkhami.f1champions.seasondetails.intrastructure.api.config

import com.elkhami.f1champions.seasondetails.intrastructure.api.SeasonDetailsClient
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import io.github.resilience4j.retry.RetryRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class SeasonDetailsClientConfig {
    @Bean
    fun seasonDetailsClient(
        webClient: WebClient,
        circuitBreakerRegistry: CircuitBreakerRegistry,
        rateLimiterRegistry: RateLimiterRegistry,
        retryRegistry: RetryRegistry,
    ): SeasonDetailsClient {
        return SeasonDetailsClient(
            webClient = webClient,
            circuitBreaker = circuitBreakerRegistry.circuitBreaker(CIRCUIT_BREAKER_NAME),
            rateLimiter = rateLimiterRegistry.rateLimiter(RATE_LIMITER_NAME),
            retry = retryRegistry.retry(RETRY_NAME),
        )
    }

    companion object {
        const val CIRCUIT_BREAKER_NAME = "season-details-client"
        const val RATE_LIMITER_NAME = "season-details-rate-limiter"
        const val RETRY_NAME = "season-details-retry"
    }
}
