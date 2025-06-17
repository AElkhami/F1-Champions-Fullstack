package com.elkhami.f1champions.core.resilience

class CompositeResiliencePolicy(private val policies: List<ResiliencePolicy>) : ResiliencePolicy {
    override suspend fun <T> execute(block: suspend () -> T): T {
        return policies.foldRight(block) { policy, acc ->
            { policy.execute(acc) }
        }.invoke()
    }
}
