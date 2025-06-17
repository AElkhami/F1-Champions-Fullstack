package com.elkhami.f1champions.champions.domain.service

import com.elkhami.f1champions.champions.domain.model.Champion

interface ChampionParser {
    fun parseChampions(json: String?): List<Champion>
}
