package com.elkhami.f1champions.core.refresh

import com.elkhami.f1champions.core.startup.AppStartupOrchestrator
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.time.Year

class RefreshServiceTest {
    private val orchestrator = mockk<AppStartupOrchestrator>(relaxed = true)
    private val refreshService = RefreshService(orchestrator)

    @Test
    fun `refreshCurrentSeason calls orchestrator with current year`() =
        runTest {
            val currentYear = Year.now().value

            refreshService.refreshCurrentSeason()

            coVerify { orchestrator.refreshChampion(currentYear) }
            coVerify { orchestrator.refreshSeasons(currentYear) }
        }
}
