package com.elkhami.f1champions.champions.data.mapper

import com.elkhami.f1champions.champions.data.model.ChampionDto
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by A.Elkhami on 23/05/2025.
 */
class ChampionDtoMapperTest {

    @Test
    fun `toChampion maps all fields correctly`() {
        val dto = ChampionDto(
            season = "2023",
            driverName = "Max Verstappen",
            constructor = "Red Bull"
        )

        val champion = dto.toChampion()

        assertEquals("2023", champion.season)
        assertEquals("Max Verstappen", champion.driverName)
        assertEquals("Red Bull", champion.constructor)
    }
}