package com.tohid.secretstash.exceptions

data class NoteNotFoundException(
    override val message: String = "Note not found"
) : RuntimeException(message)
