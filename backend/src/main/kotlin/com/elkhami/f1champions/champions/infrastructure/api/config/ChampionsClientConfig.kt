package com.elkhami.f1champions.champions.infrastructure.api.config

import com.elkhami.f1champions.champions.infrastructure.api.ChampionsClient
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import io.github.resilience4j.retry.RetryRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ChampionsClientConfig {
    @Bean
    fun championsClient(
        webClient: WebClient,
        circuitBreakerRegistry: CircuitBreakerRegistry,
        rateLimiterRegistry: RateLimiterRegistry,
        retryRegistry: RetryRegistry,
    ): ChampionsClient {
        return ChampionsClient(
            webClient = webClient,
            circuitBreaker = circuitBreakerRegistry.circuitBreaker(CIRCUIT_BREAKER_NAME),
            rateLimiter = rateLimiterRegistry.rateLimiter(RATE_LIMITER_NAME),
            retry = retryRegistry.retry(RETRY_NAME),
        )
    }

    companion object {
        const val CIRCUIT_BREAKER_NAME = "champions-client"
        const val RATE_LIMITER_NAME = "champions-rate-limiter"
        const val RETRY_NAME = "champions-retry"
    }
}
