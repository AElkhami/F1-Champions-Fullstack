package com.elkhami.f1champions.seasondetails.controller

import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1champions.seasondetails.domain.service.SeasonDetailsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/f1")
class SeasonDetailsController(private val service: SeasonDetailsService) {
    @GetMapping("/champions/{season}")
    fun getSeasonDetails(
        @PathVariable season: String,
    ): List<SeasonDetail> = service.getSeasonDetails(season)
}
