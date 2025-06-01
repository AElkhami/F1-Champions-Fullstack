package com.elkhami.f1champions.seasondetails.infrastructure.mapper

import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1champions.seasondetails.intrastructure.mapper.toDomain
import com.elkhami.f1champions.seasondetails.intrastructure.mapper.toEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SeasonDetailsTest {
    @Test
    fun `RaceResult toEntity and back should preserve data`() {
        val domain =
            SeasonDetail(
                season = "2021",
                round = "1",
                raceName = "Bahrain GP",
                date = "2021-03-28",
                winnerId = "max",
                winnerName = "Max Verstappen",
                constructor = "Red Bull",
            )

        val entity = domain.toEntity()
        val result = entity.toDomain()

        assertEquals(domain, result)
    }
}
