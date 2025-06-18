package com.elkhami.f1champions.core.resilience

import io.github.resilience4j.kotlin.ratelimiter.executeSuspendFunction
import io.github.resilience4j.ratelimiter.RateLimiter

class RateLimiterPolicy(private val rateLimiter: RateLimiter) : ResiliencePolicy {
    override suspend fun <T> execute(block: suspend () -> T): T {
        return rateLimiter.executeSuspendFunction(block)
    }
}
