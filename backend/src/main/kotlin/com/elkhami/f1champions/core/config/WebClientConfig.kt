package com.elkhami.f1champions.core.config

import io.netty.channel.ChannelOption
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Configuration
class WebClientConfig {
    @Bean
    fun webClient(
        builder: WebClient.Builder,
        @Value("\${f1.api.base-url}") baseUrl: String,
    ): WebClient = buildConfiguredWebClient(baseUrl)

    private fun buildConfiguredWebClient(baseUrl: String): WebClient {
        val httpClient =
            HttpClient.create()
                .responseTimeout(Duration.ofSeconds(RESPONSE_TIMEOUT))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECTION_TIMEOUT)

        return WebClient.builder()
            .baseUrl(baseUrl)
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .build()
    }

    companion object {
        const val RESPONSE_TIMEOUT = 5L
        const val CONNECTION_TIMEOUT = 3000
    }
}
