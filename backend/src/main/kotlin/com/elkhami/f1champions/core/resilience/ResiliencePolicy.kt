package com.elkhami.f1champions.core.resilience

interface ResiliencePolicy {
    suspend fun <T> execute(block: suspend () -> T): T
}
