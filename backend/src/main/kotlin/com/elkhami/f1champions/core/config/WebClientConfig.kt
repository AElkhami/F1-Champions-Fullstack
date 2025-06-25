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
        @Value("\${f1.api.response-timeout:5}") responseTimeout: Long,
        @Value("\${f1.api.connection-timeout:3000}") connectionTimeout: Int,
    ): WebClient = buildConfiguredWebClient(baseUrl, responseTimeout, connectionTimeout)

    private fun buildConfiguredWebClient(
        baseUrl: String,
        responseTimeout: Long,
        connectionTimeout: Int,
    ): WebClient {
        val httpClient =
            HttpClient.create()
                .responseTimeout(Duration.ofSeconds(responseTimeout))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)

        return WebClient.builder()
            .baseUrl(baseUrl)
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .build()
    }
}
