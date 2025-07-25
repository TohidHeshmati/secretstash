package com.tohid.secretstash.dtos

import java.time.Instant

data class NoteResponse(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: Instant,
    val expiresAt: Instant?
)
