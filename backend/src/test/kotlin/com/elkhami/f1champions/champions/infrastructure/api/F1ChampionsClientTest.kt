package com.elkhami.f1champions.champions.infrastructure.api

import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.champions.domain.service.ChampionParser
import com.elkhami.f1champions.core.network.ApiResponse
import com.elkhami.f1champions.core.resilience.CompositeResiliencePolicy
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

class F1ChampionsClientTest {
    private val webClient = mockk<WebClient>(relaxed = true)
    private val uriSpec = mockk<WebClient.RequestHeadersUriSpec<*>>(relaxed = true)
    private val headersSpec = mockk<WebClient.RequestHeadersSpec<*>>(relaxed = true)
    private val responseSpec = mockk<WebClient.ResponseSpec>(relaxed = true)
    private val resiliencePolicy = mockk<CompositeResiliencePolicy>()
    private val parser = mockk<ChampionParser>()

    private lateinit var client: F1ChampionsClient

    @BeforeTest
    fun setup() {
        every { webClient.get() } returns uriSpec

        every { uriSpec.uri(any<Function<UriBuilder, URI>>()) } returns headersSpec

        every { headersSpec.retrieve() } returns responseSpec

        client =
            F1ChampionsClient(
                webClient = webClient,
                resiliencePolicy = resiliencePolicy,
                parser = parser,
            )
    }

    @Test
    fun `fetchFromApi returns parsed champion`() =
        runTest {
            val json = """{ "mock": "response" }"""

            every { responseSpec.bodyToMono(String::class.java) } returns Mono.just(json)

            every { parser.parseChampions(json) } returns
                listOf(
                    Champion("2020", "hamilton", "Lewis Hamilton", "Mercedes"),
                )

            val result = client.fetchFromApi(2020)
            val parsedResult = parser.parseChampions(result)

            assertNotNull(result)
            assertEquals("2020", parsedResult.first().season)
            assertEquals("hamilton", parsedResult.first().driverId)
        }

    @Test
    fun `fetchChampion returns champion using resilientCall`() =
        runTest {
            val expectedChampion = Champion("2021", "verstappen", "Max Verstappen", "Red Bull")

            val json = """{ "mock": "response" }"""

            coEvery {
                resiliencePolicy.execute<Champion?>(any())
            } coAnswers {
                firstArg<suspend () -> Champion?>().invoke()
            }
            every { responseSpec.bodyToMono(String::class.java) } returns Mono.just(json)
            every { parser.parseChampions(json) } returns listOf(expectedChampion)

            val result = client.fetchChampion(2021)

            assertNotNull(result)
            assert(result is ApiResponse.Success)
            assertEquals(expectedChampion, (result as ApiResponse.Success).data)
        }

    @Test
    fun `fetchChampion returns error response on failure`() =
        runTest {
            every { responseSpec.bodyToMono(String::class.java) } throws RuntimeException("API down")

            val result = client.fetchChampion(2022)

            assertNotNull(result)
            assert(result is ApiResponse.Error)
        }
}
