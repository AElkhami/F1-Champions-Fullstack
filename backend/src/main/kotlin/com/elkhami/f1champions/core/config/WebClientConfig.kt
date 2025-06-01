package com.elkhami.f1champions.core.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean
    fun webClient(
        builder: WebClient.Builder,
        @Value("\${f1.api.base-url}") baseUrl: String,
    ): WebClient = builder.baseUrl(baseUrl).build()
}
