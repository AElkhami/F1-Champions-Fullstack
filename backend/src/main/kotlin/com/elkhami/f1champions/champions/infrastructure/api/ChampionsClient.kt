package com.elkhami.f1champions.champions.infrastructure.api

import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.core.logger.loggerWithPrefix
import com.elkhami.f1champions.core.resilience4j.resilientCall
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.retry.Retry
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class ChampionsClient(
    private val webClient: WebClient,
    private val circuitBreaker: CircuitBreaker,
    private val rateLimiter: RateLimiter,
    private val retry: Retry,
) {
    private val logger = loggerWithPrefix()

    suspend fun fetchChampion(year: Int): Champion? {
        return runCatching {
            resilientCall(
                retry = retry,
                rateLimiter = rateLimiter,
                circuitBreaker = circuitBreaker,
            ) {
                fetchFromApi(year)?.also {
                    logger.info("✅ Got champion for $year")
                }
            }
        }.getOrElse {
            logger.warn("⚠️ Failed to fetch champion for $year: ${it.message}")
            null
        }
    }

    internal suspend fun fetchFromApi(year: Int): Champion? {
        val response =
            webClient
                .get()
                .uri { it.pathSegment(year.toString(), "driverStandings", "1.json").build() }
                .retrieve()
                .bodyToMono(String::class.java)
                .awaitSingle()

        return ChampionParser.parseChampions(response).firstOrNull()
    }
}
