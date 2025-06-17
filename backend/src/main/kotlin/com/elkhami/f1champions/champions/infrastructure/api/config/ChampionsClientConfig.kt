package com.elkhami.f1champions.champions.infrastructure.api.config

import com.elkhami.f1champions.champions.domain.service.ChampionsClient
import com.elkhami.f1champions.champions.infrastructure.api.F1ChampionParser
import com.elkhami.f1champions.champions.infrastructure.api.F1ChampionsClient
import com.elkhami.f1champions.core.resilience.CompositeResiliencePolicy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ChampionsClientConfig {
    @Bean
    fun championsClient(
        webClient: WebClient,
        resiliencePolicy: CompositeResiliencePolicy,
        parser: F1ChampionParser,
    ): ChampionsClient {
        return F1ChampionsClient(
            webClient = webClient,
            resiliencePolicy = resiliencePolicy,
            parser = parser,
        )
    }
}
