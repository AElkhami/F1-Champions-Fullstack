package com.elkhami.f1champions.champions.controller

import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.champions.domain.service.ChampionsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/f1")
class ChampionsController(private val service: ChampionsService) {
    @GetMapping("/champions")
    fun getChampions(): List<Champion> = service.getChampions()
}
