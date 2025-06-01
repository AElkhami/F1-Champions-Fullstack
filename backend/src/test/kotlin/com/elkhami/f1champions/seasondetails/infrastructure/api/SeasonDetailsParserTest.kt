package com.elkhami.f1champions.seasondetails.infrastructure.api

import com.elkhami.f1champions.seasondetails.intrastructure.api.SeasonDetailsParser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SeasonDetailsParserTest {
    @Test
    fun `parseRaceWinners should return list of RaceResult`() {
        val json =
            """
            {
              "MRData": {
                "RaceTable": {
                  "Races": [
                    {
                      "season": "2021",
                      "round": "1",
                      "raceName": "Bahrain Grand Prix",
                      "date": "2021-03-28",
                      "Results": [
                        {
                          "Driver": {
                            "driverId": "max",
                            "givenName": "Max",
                            "familyName": "Verstappen"
                          },
                          "Constructor": {
                            "name": "Red Bull"
                          }
                        }
                      ]
                    }
                  ]
                }
              }
            }
            """.trimIndent()

        val result = SeasonDetailsParser.parseSeasonDetails("2021", json)

        assertEquals(1, result.size)
        val race = result[0]
        assertEquals("2021", race.season)
        assertEquals("1", race.round)
        assertEquals("Bahrain Grand Prix", race.raceName)
        assertEquals("2021-03-28", race.date)
        assertEquals("max", race.winnerId)
        assertEquals("Max Verstappen", race.winnerName)
        assertEquals("Red Bull", race.constructor)
    }
}
