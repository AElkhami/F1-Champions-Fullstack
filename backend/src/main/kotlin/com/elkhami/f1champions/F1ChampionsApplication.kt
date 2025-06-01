package com.elkhami.f1champions

import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean

@EnableCaching
@SpringBootApplication
class F1ChampionsApplication

@Bean
fun rateLimiterRegistry(): RateLimiterRegistry = RateLimiterRegistry.ofDefaults()

fun main(args: Array<String>) {
    runApplication<F1ChampionsApplication>(*args)
}
