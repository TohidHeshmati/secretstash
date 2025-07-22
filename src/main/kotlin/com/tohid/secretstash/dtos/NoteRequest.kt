package com.tohid.secretstash.dtos

import java.time.Instant

data class NoteRequest(
    val title: String,
    val content: String,
    val expiresAt: Instant? = null
)
