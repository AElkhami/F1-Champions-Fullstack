package com.elkhami.f1champions.seasondetails.infrastructure.api

import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.core.resilience.CompositeResiliencePolicy
import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1champions.seasondetails.domain.service.SeasonDetailsParser
import com.elkhami.f1champions.seasondetails.intrastructure.api.F1SeasonDetailsClient
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Mono
import java.net.URI
import java.util.function.Function
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class F1SeasonDetailsClientTest {
    private val webClient = mockk<WebClient>(relaxed = true)
    private val uriSpec = mockk<WebClient.RequestHeadersUriSpec<*>>(relaxed = true)
    private val headersSpec = mockk<WebClient.RequestHeadersSpec<*>>(relaxed = true)
    private val responseSpec = mockk<WebClient.ResponseSpec>(relaxed = true)
    private val resiliencePolicy = mockk<CompositeResiliencePolicy>()
    private val parser = mockk<SeasonDetailsParser>()

    private lateinit var client: F1SeasonDetailsClient

    @BeforeTest
    fun setup() {
        every { webClient.get() } returns uriSpec
        every { uriSpec.uri(any<Function<UriBuilder, URI>>()) } returns headersSpec
        every { headersSpec.retrieve() } returns responseSpec

        client =
            F1SeasonDetailsClient(
                webClient = webClient,
                resiliencePolicy = resiliencePolicy,
                parser = parser,
            )
    }

    @Test
    fun `fetchFromApi returns parsed season details`() =
        runTest {
            val json = """{ "mock": "response" }"""

            every { responseSpec.bodyToMono(String::class.java) } returns Mono.just(json)

            every { parser.parseSeasonDetails("2020", json) } returns
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
            val parsedResult = parser.parseSeasonDetails("2020", result)

            assertNotNull(result)
            assertEquals("2020", parsedResult.first().season)
            assertEquals("Austrian Grand Prix", parsedResult.first().raceName)
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

            coEvery {
                resiliencePolicy.execute<List<Champion>>(any())
            } coAnswers {
                firstArg<suspend () -> List<Champion>>().invoke()
            }
            every { responseSpec.bodyToMono(String::class.java) } returns Mono.just(json)
            every { parser.parseSeasonDetails("2021", json) } returns listOf(expected)

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
