package com.elkhami.f1champions.champions.infrastructure.api

import com.elkhami.f1champions.champions.domain.model.Champion
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.retry.Retry
import io.github.resilience4j.retry.RetryConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertNull
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Mono
import java.net.URI
import java.time.Duration
import java.util.function.Function
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ChampionsClientTest {
    private val webClient = mockk<WebClient>(relaxed = true)
    private val uriSpec = mockk<WebClient.RequestHeadersUriSpec<*>>(relaxed = true)
    private val headersSpec = mockk<WebClient.RequestHeadersSpec<*>>(relaxed = true)
    private val responseSpec = mockk<WebClient.ResponseSpec>(relaxed = true)

    private lateinit var client: ChampionsClient

    @BeforeTest
    fun setup() {
        val retry =
            Retry.of(
                "test",
                RetryConfig.custom<Any>()
                    .maxAttempts(2)
                    .waitDuration(Duration.ofMillis(10))
                    .build(),
            )

        val rateLimiter = RateLimiter.ofDefaults("testRateLimiter")
        val circuitBreaker = CircuitBreaker.ofDefaults("testCircuitBreaker")

        every { webClient.get() } returns uriSpec

        every { uriSpec.uri(any<Function<UriBuilder, URI>>()) } returns headersSpec

        every { headersSpec.retrieve() } returns responseSpec

        client =
            ChampionsClient(
                webClient = webClient,
                circuitBreaker = circuitBreaker,
                rateLimiter = rateLimiter,
                retry = retry,
            )
    }

    @Test
    fun `fetchFromApi returns parsed champion`() =
        runTest {
            val json = """{ "mock": "response" }"""

            every { responseSpec.bodyToMono(String::class.java) } returns Mono.just(json)

            mockkObject(ChampionParser)
            every { ChampionParser.parseChampions(json) } returns
                listOf(
                    Champion("2020", "hamilton", "Lewis Hamilton", "Mercedes"),
                )

            val result = client.fetchFromApi(2020)

            assertNotNull(result)
            assertEquals("2020", result.season)
            assertEquals("hamilton", result.driverId)
        }

    @Test
    fun `fetchChampion returns champion using resilientCall`() =
        runTest {
            val expectedChampion = Champion("2021", "verstappen", "Max Verstappen", "Red Bull")

            mockkObject(ChampionParser)
            val json = """{ "mock": "response" }"""
            every { responseSpec.bodyToMono(String::class.java) } returns Mono.just(json)
            every { ChampionParser.parseChampions(json) } returns listOf(expectedChampion)

            val result = client.fetchChampion(2021)

            assertNotNull(result)
            assertEquals(expectedChampion, result)
        }

    @Test
    fun `fetchChampion returns null and logs on failure`() =
        runTest {
            every { responseSpec.bodyToMono(String::class.java) } throws RuntimeException("API down")

            val result = client.fetchChampion(2022)

            assertNull(result)
        }
}
