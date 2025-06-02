package com.elkhami.f1champions.champions.infrastructure.mapper

import com.elkhami.f1champions.champions.domain.model.Champion
import kotlin.test.Test
import kotlin.test.assertEquals

class ChampionsMapperTest {
    @Test
    fun `Champion toEntity and back should preserve data`() {
        val domain =
            Champion(
                season = "2021",
                driverId = "max",
                driverName = "Max Verstappen",
                constructor = "Red Bull",
            )

        val entity = domain.toEntity()
        val result = entity.toDomain()

        assertEquals(domain, result)
    }
}
