package com.elkhami.f1champions.core.resilience

import io.github.resilience4j.kotlin.retry.executeSuspendFunction
import io.github.resilience4j.retry.Retry

class RetryPolicy(private val retry: Retry) : ResiliencePolicy {
    override suspend fun <T> execute(block: suspend () -> T): T {
        return retry.executeSuspendFunction(block)
    }
}
