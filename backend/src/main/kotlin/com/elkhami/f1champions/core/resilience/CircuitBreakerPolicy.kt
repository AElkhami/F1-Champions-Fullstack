package com.elkhami.f1champions.core.resilience

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.kotlin.circuitbreaker.executeSuspendFunction

class CircuitBreakerPolicy(private val circuitBreaker: CircuitBreaker) : ResiliencePolicy {
    override suspend fun <T> execute(block: suspend () -> T): T {
        return circuitBreaker.executeSuspendFunction(block)
    }
}
