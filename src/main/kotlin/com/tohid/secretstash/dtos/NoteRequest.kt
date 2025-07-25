package com.tohid.secretstash.dtos

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.tohid.secretstash.utils.SafeInstantDeserializer
import com.tohid.secretstash.validators.FutureInstant
import java.time.Instant

data class NoteRequest(
    val title: String,
    val content: String,
    @field:FutureInstant
    @field:JsonDeserialize(using = SafeInstantDeserializer::class)
    val expiresAt: Instant? = null
)
