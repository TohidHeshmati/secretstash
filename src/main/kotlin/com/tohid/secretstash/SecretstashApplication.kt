package com.tohid.secretstash

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties
class SecretstashApplication

fun main(args: Array<String>) {
    runApplication<SecretstashApplication>(*args)
}
