package com.elkhami.f1champions.core.resilience4j

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.kotlin.circuitbreaker.executeSuspendFunction
import io.github.resilience4j.kotlin.ratelimiter.executeSuspendFunction
import io.github.resilience4j.kotlin.retry.executeSuspendFunction
import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.retry.Retry

suspend fun <T> resilientCall(
    retry: Retry,
    rateLimiter: RateLimiter,
    circuitBreaker: CircuitBreaker,
    block: suspend () -> T,
): T {
    return retry.executeSuspendFunction {
        rateLimiter.executeSuspendFunction {
            circuitBreaker.executeSuspendFunction {
                block()
            }
        }
    }
}
