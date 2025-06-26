package com.elkhami.f1champions.seasondetails.infrastructure.api

import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1champions.seasondetails.intrastructure.api.F1SeasonDetailsParser
import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class F1SeasonDetailsParserTest {
    private val objectMapper = ObjectMapper()
    private val parser = F1SeasonDetailsParser(objectMapper)

    @Test
    fun `parseSeasonDetails should return list of SeasonDetail`() {
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

        val result: List<SeasonDetail> = parser.parseSeasonDetails("2021", json)

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

    @Test
    fun `parseSeasonDetails should return empty list for null input`() {
        val result = parser.parseSeasonDetails("2021", null)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `parseSeasonDetails should return empty list for blank input`() {
        val result = parser.parseSeasonDetails("2021", "")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `parseSeasonDetails should return empty list for invalid JSON`() {
        val result = parser.parseSeasonDetails("2021", "{ invalid json }")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `parseSeasonDetails should handle missing race data`() {
        val json =
            """
            {
              "MRData": {
                "RaceTable": {
                  "Races": []
                }
              }
            }
            """.trimIndent()

        val result = parser.parseSeasonDetails("2021", json)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `parseSeasonDetails should handle missing results`() {
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
                      "Results": []
                    }
                  ]
                }
              }
            }
            """.trimIndent()

        val result = parser.parseSeasonDetails("2021", json)
        assertTrue(result.isEmpty())
    }
}
