package com.tohid.secretstash.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "app.jwt")
data class JwtProperties @ConstructorBinding constructor(
    val secret: String,
    val expirationMs: Long
)
