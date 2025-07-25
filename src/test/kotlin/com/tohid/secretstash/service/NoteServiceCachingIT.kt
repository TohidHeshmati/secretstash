package com.tohid.secretstash.service;

import com.tohid.secretstash.BaseIntegrationTest
import com.tohid.secretstash.dtos.NoteRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class NoteServiceCachingIT : BaseIntegrationTest() {

    @Test
    fun `should cache getMyNotes and evict after create`() {
        val note1 = noteService.createNote(NoteRequest("Title 1", "Content 1", null))

        val firstCall = noteService.getMyNotes()
        val secondCall = noteService.getMyNotes()

        assertThat(firstCall).isEqualTo(secondCall) // From cache

        // Add another note
        noteService.createNote(NoteRequest("Title 2", "Content 2", null))

        val afterCreate = noteService.getMyNotes()
        assertThat(afterCreate).hasSize(2) // Cache evicted, data reloaded
    }
}
