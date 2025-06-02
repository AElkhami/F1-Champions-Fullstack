package com.elkhami.f1champions

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnableCaching
@SpringBootApplication
class F1ChampionsApplication

fun main(args: Array<String>) {
    runApplication<F1ChampionsApplication>(*args)
}
