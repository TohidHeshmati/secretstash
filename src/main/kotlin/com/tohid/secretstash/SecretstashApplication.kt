package com.tohid.secretstash

import com.tohid.secretstash.config.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(JwtProperties::class)
class SecretstashApplication

fun main(args: Array<String>) {
    runApplication<SecretstashApplication>(*args)
}
