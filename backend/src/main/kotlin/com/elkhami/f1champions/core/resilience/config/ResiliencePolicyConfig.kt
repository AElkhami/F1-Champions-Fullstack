package com.elkhami.f1champions.core.resilience.config

import com.elkhami.f1champions.core.resilience.CircuitBreakerPolicy
import com.elkhami.f1champions.core.resilience.CompositeResiliencePolicy
import com.elkhami.f1champions.core.resilience.RateLimiterPolicy
import com.elkhami.f1champions.core.resilience.RetryPolicy
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import io.github.resilience4j.retry.RetryRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ResiliencePolicyConfig {
    @Bean
    fun resiliencePolicy(
        circuitBreakerRegistry: CircuitBreakerRegistry,
        rateLimiterRegistry: RateLimiterRegistry,
        retryRegistry: RetryRegistry,
    ): CompositeResiliencePolicy {
        val circuitBreaker = circuitBreakerRegistry.circuitBreaker(CIRCUIT_BREAKER_NAME)
        val rateLimiter = rateLimiterRegistry.rateLimiter(RATE_LIMITER_NAME)
        val retry = retryRegistry.retry(RETRY_NAME)

        return CompositeResiliencePolicy(
            listOf(
                RetryPolicy(retry),
                RateLimiterPolicy(rateLimiter),
                CircuitBreakerPolicy(circuitBreaker),
            ),
        )
    }

    companion object {
        const val CIRCUIT_BREAKER_NAME = "circuit-breaker"
        const val RATE_LIMITER_NAME = "rate-limiter"
        const val RETRY_NAME = "retry"
    }
}
