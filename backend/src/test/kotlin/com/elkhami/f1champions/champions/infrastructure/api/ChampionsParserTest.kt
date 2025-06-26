package com.elkhami.f1champions.champions.infrastructure.api

import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChampionsParserTest {
    private val objectMapper = ObjectMapper()
    private val parser = F1ChampionParser(objectMapper)

    @Test
    fun `parseChampions should return list of Champion`() {
        val json =
            """
            {
              "MRData": {
                "StandingsTable": {
                  "StandingsLists": [
                    {
                      "season": "2021",
                      "DriverStandings": [
                        {
                          "Driver": {
                            "driverId": "max",
                            "givenName": "Max",
                            "familyName": "Verstappen"
                          },
                          "Constructors": [
                            {
                              "name": "Red Bull"
                            }
                          ]
                        }
                      ]
                    }
                  ]
                }
              }
            }
            """.trimIndent()

        val result = parser.parseChampions(json)

        assertEquals(1, result.size)
        val champ = result[0]
        assertEquals("2021", champ.season)
        assertEquals("max", champ.driverId)
        assertEquals("Max Verstappen", champ.driverName)
        assertEquals("Red Bull", champ.constructor)
    }

    @Test
    fun `parseChampions should return empty list for null input`() {
        val result = parser.parseChampions(null)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `parseChampions should return empty list for blank input`() {
        val result = parser.parseChampions("")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `parseChampions should return empty list for invalid JSON`() {
        val result = parser.parseChampions("{ invalid json }")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `parseChampions should handle missing standings data`() {
        val json =
            """
            {
              "MRData": {
                "StandingsTable": {
                  "StandingsLists": []
                }
              }
            }
            """.trimIndent()

        val result = parser.parseChampions(json)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `parseChampions should handle missing driver standings`() {
        val json =
            """
            {
              "MRData": {
                "StandingsTable": {
                  "StandingsLists": [
                    {
                      "season": "2021",
                      "DriverStandings": []
                    }
                  ]
                }
              }
            }
            """.trimIndent()

        val result = parser.parseChampions(json)
        assertTrue(result.isEmpty())
    }
}
