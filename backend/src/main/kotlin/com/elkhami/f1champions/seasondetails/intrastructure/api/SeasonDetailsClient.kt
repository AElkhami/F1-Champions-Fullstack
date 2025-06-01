package com.elkhami.f1champions.seasondetails.intrastructure.api

import com.elkhami.f1champions.core.logger.loggerWithPrefix
import com.elkhami.f1champions.core.resilience4j.resilientCall
import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.retry.Retry
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class SeasonDetailsClient(
    private val webClient: WebClient,
    private val circuitBreaker: CircuitBreaker,
    private val rateLimiter: RateLimiter,
    private val retry: Retry,
) {
    private val logger = loggerWithPrefix()

    suspend fun fetchSeasonDetails(season: String): List<SeasonDetail> {
        return runCatching {
            resilientCall(
                retry = retry,
                rateLimiter = rateLimiter,
                circuitBreaker = circuitBreaker,
            ) {
                fetchFromApi(season)
                    .also { logger.info("✅ Got ${it.size} races for $season") }
            }
        }.getOrElse {
            logger.warn("⚠️ Failed to fetch details for $season: ${it.message}")
            emptyList()
        }
    }

    internal suspend fun fetchFromApi(season: String): List<SeasonDetail> {
        val response =
            webClient
                .get()
                .uri { it.pathSegment(season, "results", "1.json").build() }
                .retrieve()
                .bodyToMono(String::class.java)
                .awaitSingle()

        return SeasonDetailsParser.parseSeasonDetails(season, response)
    }
}
