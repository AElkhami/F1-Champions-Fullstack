package com.elkhami.f1champions.core.seeding

import com.elkhami.f1champions.core.startup.AppStartupOrchestrator
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.time.Year

class SeedingServiceTest {
    private val orchestrator = mockk<AppStartupOrchestrator>(relaxed = true)
    private val seedingService = SeedingService(orchestrator)

    @Test
    fun `refreshCurrentSeason calls orchestrator with current year`() =
        runTest {
            val currentYear = Year.now().value

            seedingService.refreshCurrentSeason()

            coVerify { orchestrator.refreshChampion(currentYear) }
            coVerify { orchestrator.refreshSeasons(currentYear) }
        }
}
