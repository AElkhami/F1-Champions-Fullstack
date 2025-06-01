package com.elkhami.f1champions.seasondetails.infrastructure.api

import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1champions.seasondetails.intrastructure.api.SeasonDetailsClient
import com.elkhami.f1champions.seasondetails.intrastructure.api.SeasonDetailsParser
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.retry.Retry
import io.github.resilience4j.retry.RetryConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Mono
import java.net.URI
import java.time.Duration
import java.util.function.Function
import kotlin.test.BeforeTest
import kotlin.test.Test

class SeasonDetailsClientTest {
    private val webClient = mockk<WebClient>(relaxed = true)
    private val uriSpec = mockk<WebClient.RequestHeadersUriSpec<*>>(relaxed = true)
    private val headersSpec = mockk<WebClient.RequestHeadersSpec<*>>(relaxed = true)
    private val responseSpec = mockk<WebClient.ResponseSpec>(relaxed = true)

    private lateinit var client: SeasonDetailsClient

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
            SeasonDetailsClient(
                webClient = webClient,
                circuitBreaker = circuitBreaker,
                rateLimiter = rateLimiter,
                retry = retry,
            )
    }

    @Test
    fun `fetchFromApi returns parsed season details`() =
        runTest {
            val json = """{ "mock": "response" }"""

            every { responseSpec.bodyToMono(String::class.java) } returns Mono.just(json)

            mockkObject(SeasonDetailsParser)
            every { SeasonDetailsParser.parseSeasonDetails("2020", json) } returns
                listOf(
                    SeasonDetail(
                        season = "2020",
                        round = "1",
                        raceName = "Austrian Grand Prix",
                        date = "2020-07-05",
                        winnerId = "bottas",
                        winnerName = "Valtteri Bottas",
                        constructor = "Mercedes",
                    ),
                )

            val result = client.fetchFromApi("2020")

            assertNotNull(result)
            assertEquals("2020", result.first().season)
            assertEquals("Austrian Grand Prix", result.first().raceName)
        }

    @Test
    fun `fetchSeasonDetails returns season details using resilientCall`() =
        runTest {
            val expected =
                SeasonDetail(
                    season = "2021",
                    round = "1",
                    raceName = "Bahrain Grand Prix",
                    date = "2021-03-28",
                    winnerId = "hamilton",
                    winnerName = "Lewis Hamilton",
                    constructor = "Mercedes",
                )

            val json = """{ "mock": "response" }"""

            mockkObject(SeasonDetailsParser)
            every { responseSpec.bodyToMono(String::class.java) } returns Mono.just(json)
            every { SeasonDetailsParser.parseSeasonDetails("2021", json) } returns listOf(expected)

            val result = client.fetchSeasonDetails("2021")

            assertNotNull(result)
            assertEquals(expected, result.first())
        }

    @Test
    fun `fetchSeasonDetails returns empty list on failure`() =
        runTest {
            every { responseSpec.bodyToMono(String::class.java) } throws RuntimeException("API failure")

            val result = client.fetchSeasonDetails("2022")

            assertNotNull(result)
            assert(result.isEmpty())
        }
}
