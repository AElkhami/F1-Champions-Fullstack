package com.elkhami.f1champions.champions.infrastructure.api

import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.champions.domain.service.ChampionParser
import com.elkhami.f1champions.champions.domain.service.ChampionsClient
import com.elkhami.f1champions.core.logger.loggerWithPrefix
import com.elkhami.f1champions.core.network.ApiResponse
import com.elkhami.f1champions.core.resilience.CompositeResiliencePolicy
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class F1ChampionsClient(
    private val webClient: WebClient,
    private val resiliencePolicy: CompositeResiliencePolicy,
    private val parser: ChampionParser,
) : ChampionsClient {
    private val logger = this.loggerWithPrefix()

    override suspend fun fetchChampion(year: Int): ApiResponse<Champion?> {
        return runCatching {
            resiliencePolicy.execute {
                fetchFromApi(year)?.let { json ->
                    parser.parseChampions(json).firstOrNull()?.also {
                        logger.info("✅ Got champion for $year")
                    }
                }
            }
        }.fold(
            onSuccess = { champion ->
                ApiResponse.success(champion)
            },
            onFailure = { exception ->
                logger.warn("⚠️ Failed to fetch champion for $year: ${exception.message}")
                ApiResponse.error("Failed to fetch champion for $year", exception)
            },
        )
    }

    internal suspend fun fetchFromApi(year: Int): String? {
        return webClient
            .get()
            .uri { it.pathSegment(year.toString(), "driverStandings", "1.json").build() }
            .retrieve()
            .bodyToMono(String::class.java)
            .awaitSingle()
    }
}
