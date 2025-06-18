package com.elkhami.f1champions.seasondetails.intrastructure.api

import com.elkhami.f1champions.core.logger.loggerWithPrefix
import com.elkhami.f1champions.core.resilience.CompositeResiliencePolicy
import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1champions.seasondetails.domain.service.SeasonDetailsClient
import com.elkhami.f1champions.seasondetails.domain.service.SeasonDetailsParser
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class F1SeasonDetailsClient(
    private val webClient: WebClient,
    private val resiliencePolicy: CompositeResiliencePolicy,
    private val parser: SeasonDetailsParser,
) : SeasonDetailsClient {
    private val logger = loggerWithPrefix()

    override suspend fun fetchSeasonDetails(season: String): List<SeasonDetail>? {
        return runCatching {
            resiliencePolicy.execute {
                fetchFromApi(season)?.let { json ->
                    parser.parseSeasonDetails(season, json).also {
                        logger.info("✅ Got ${it.size} races for $season")
                    }
                }
            }
        }.getOrElse {
            logger.warn("⚠️ Failed to fetch details for $season: ${it.message}")
            emptyList()
        }
    }

    internal suspend fun fetchFromApi(season: String): String? {
        return webClient
            .get()
            .uri { it.pathSegment(season, "results", "1.json").build() }
            .retrieve()
            .bodyToMono(String::class.java)
            .awaitSingle()
    }
}
