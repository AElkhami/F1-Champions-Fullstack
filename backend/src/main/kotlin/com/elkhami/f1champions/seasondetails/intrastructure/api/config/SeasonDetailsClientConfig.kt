package com.elkhami.f1champions.seasondetails.intrastructure.api.config

import com.elkhami.f1champions.core.resilience.CompositeResiliencePolicy
import com.elkhami.f1champions.seasondetails.domain.service.SeasonDetailsClient
import com.elkhami.f1champions.seasondetails.intrastructure.api.F1SeasonDetailsClient
import com.elkhami.f1champions.seasondetails.intrastructure.api.F1SeasonDetailsParser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class SeasonDetailsClientConfig {
    @Bean
    fun seasonDetailsClient(
        webClient: WebClient,
        resiliencePolicy: CompositeResiliencePolicy,
        parser: F1SeasonDetailsParser,
    ): SeasonDetailsClient {
        return F1SeasonDetailsClient(
            webClient = webClient,
            resiliencePolicy = resiliencePolicy,
            parser = parser,
        )
    }
}
