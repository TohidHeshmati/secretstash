package com.tohid.secretstash.dtos

data class PagedNoteResponse(
    val content: List<NoteResponse>,
    val totalElements: Long,
    val totalPages: Int,
    val currentPage: Int,
    val size: Int,
    val first: Boolean,
    val last: Boolean
)
