package com.tohid.secretstash.dtos

data class JwtResponse(
    val token: String,
    val tokenType: String = "Bearer"
)
