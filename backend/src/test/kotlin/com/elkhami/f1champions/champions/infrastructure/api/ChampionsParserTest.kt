package com.elkhami.f1champions.champions.infrastructure.api

import kotlin.test.Test
import kotlin.test.assertEquals

class ChampionsParserTest {
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

        val result = F1ChampionParser().parseChampions(json)

        assertEquals(1, result.size)
        val champ = result[0]
        assertEquals("2021", champ.season)
        assertEquals("max", champ.driverId)
        assertEquals("Max Verstappen", champ.driverName)
        assertEquals("Red Bull", champ.constructor)
    }
}
