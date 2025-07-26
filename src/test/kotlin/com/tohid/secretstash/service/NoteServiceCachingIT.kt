package com.tohid.secretstash.service

import com.tohid.secretstash.BaseIntegrationTest
import com.tohid.secretstash.dtos.NoteRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

class NoteServiceCachingIT : BaseIntegrationTest() {
    @Test
    fun `should cache getMyNotes and evict after create`() {
        noteService.createNote(NoteRequest("Title 1", "Content 1", null))

        val firstCall = noteService.getMyNotes()
        val secondCall = noteService.getMyNotes()

        assertThat(firstCall).isEqualTo(secondCall) // From cache

        // Add another note
        noteService.createNote(NoteRequest("Title 2", "Content 2", null))

        val afterCreate = noteService.getMyNotes()
        assertThat(afterCreate).hasSize(2) // Cache evicted, data reloaded
    }

    @Test
    fun `should cache getMyNotesPaginated and evict after create`() {
        // Create initial note
        noteService.createNote(NoteRequest("Paginated Title 1", "Content 1", null))

        // Define pageable
        val pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"))

        // First call should hit the database
        val firstCall = noteService.getMyNotesPaginated(pageable)
        // Second call should be from cache
        val secondCall = noteService.getMyNotesPaginated(pageable)

        // Both calls should return the same data
        assertThat(firstCall).isEqualTo(secondCall)
        assertThat(firstCall.content).hasSize(1)

        // Create another note
        noteService.createNote(NoteRequest("Paginated Title 2", "Content 2", null))

        // Next call should hit the database again (cache evicted)
        val afterCreate = noteService.getMyNotesPaginated(pageable)
        assertThat(afterCreate.content).hasSize(2)
    }

    @Test
    fun `should cache different pagination parameters separately`() {
        // Create some notes
        for (i in 1..5) {
            noteService.createNote(NoteRequest("Title $i", "Content $i", null))
        }

        // Define different pageables
        val pageable1 = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"))
        val pageable2 = PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, "createdAt"))

        // First calls should hit the database
        val firstPage = noteService.getMyNotesPaginated(pageable1)
        val secondPage = noteService.getMyNotesPaginated(pageable2)

        // Verify different results
        assertThat(firstPage.currentPage).isEqualTo(0)
        assertThat(secondPage.currentPage).isEqualTo(1)
        assertThat(firstPage.content).hasSize(2)
        assertThat(secondPage.content).hasSize(2)
        assertThat(firstPage.content).isNotEqualTo(secondPage.content)

        // Second calls should be from cache
        val firstPageAgain = noteService.getMyNotesPaginated(pageable1)
        val secondPageAgain = noteService.getMyNotesPaginated(pageable2)

        // Verify cached results
        assertThat(firstPageAgain).isEqualTo(firstPage)
        assertThat(secondPageAgain).isEqualTo(secondPage)
    }

    @Test
    fun `should evict paginated cache after update and delete`() {
        // Create notes
        val note1 = noteService.createNote(NoteRequest("Update Title", "Original Content", null))
        noteService.createNote(NoteRequest("Delete Title", "Delete Content", null))

        val pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"))

        // First call to cache
        val initialCall = noteService.getMyNotesPaginated(pageable)
        assertThat(initialCall.content).hasSize(2)

        // Update a note
        noteService.updateNote(note1.id, NoteRequest("Updated Title", "Updated Content", null))

        // Cache should be evicted
        val afterUpdate = noteService.getMyNotesPaginated(pageable)
        assertThat(afterUpdate.content).hasSize(2)
        // Find the updated note by ID
        val updatedNote = afterUpdate.content.find { it.id == note1.id }
        assertThat(updatedNote).isNotNull
        assertThat(updatedNote?.title).isEqualTo("Updated Title")

        // Delete a note
        noteService.deleteNote(note1.id)

        // Cache should be evicted again
        val afterDelete = noteService.getMyNotesPaginated(pageable)
        assertThat(afterDelete.content).hasSize(1)
        assertThat(afterDelete.content[0].title).isEqualTo("Delete Title")
    }
}
