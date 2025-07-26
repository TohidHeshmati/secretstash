package com.tohid.secretstash.service

import com.tohid.secretstash.domain.Note
import com.tohid.secretstash.domain.User
import com.tohid.secretstash.dtos.NoteRequest
import com.tohid.secretstash.dtos.NoteResponse
import com.tohid.secretstash.dtos.PagedNoteResponse
import com.tohid.secretstash.repository.NoteRepository
import com.tohid.secretstash.repository.UserRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.Instant.now

@Service
class NoteService(
    private val noteRepository: NoteRepository,
    private val userRepository: UserRepository
) {
    fun getCurrentUser(): User {
        val authentication =
            SecurityContextHolder.getContext().authentication
                ?: throw IllegalStateException("No authentication found in context")

        val username =
            authentication.principal as? String
                ?: throw IllegalStateException("Invalid principal in authentication")

        return userRepository.findByUsername(username)
            ?: throw IllegalStateException("User not found")
    }

    @CacheEvict(value = ["notes", "paginatedNotes"], key = "#root.target.getCurrentUser().username", allEntries = true)
    fun createNote(request: NoteRequest): NoteResponse {
        val user = getCurrentUser()

        val note =
            Note(
                title = request.title,
                content = request.content,
                expiresAt = request.expiresAt,
                user = user
            )
        return noteRepository.save(note).toDto()
    }

    @Cacheable(value = ["notes"], key = "#root.target.getCurrentUser().username")
    fun getMyNotes(): List<NoteResponse> {
        val user = getCurrentUser()
        return noteRepository
            .findByUserAndExpiresAtAfterOrExpiresAtIsNullOrderByCreatedAtDesc(user)
            .take(1000)
            .map { it.toDto() }
    }

    @Cacheable(
        value = ["paginatedNotes"],
        key = "#root.target.getCurrentUser().username + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort"
    )
    fun getMyNotesPaginated(pageable: Pageable): PagedNoteResponse {
        val user = getCurrentUser()
        val page = noteRepository
            .findByUserAndExpiresAtAfterOrExpiresAtIsNullOrderByCreatedAtDesc(user, now(), pageable)

        return PagedNoteResponse(
            content = page.content.map { it.toDto() },
            totalElements = page.totalElements,
            totalPages = page.totalPages,
            currentPage = page.number,
            size = page.size,
            first = page.isFirst,
            last = page.isLast
        )
    }

    fun getNoteById(id: Long): NoteResponse {
        val user = getCurrentUser()
        val note =
            noteRepository.findValidNoteByIdAndUser(id, user)
                ?: throw IllegalArgumentException("Note not found")
        return note.toDto()
    }

    @CacheEvict(value = ["notes", "paginatedNotes"], key = "#root.target.getCurrentUser().username", allEntries = true)
    fun updateNote(
        id: Long,
        request: NoteRequest
    ): NoteResponse {
        val user = getCurrentUser()
        val note =
            noteRepository.findValidNoteByIdAndUser(id, user)
                ?: throw IllegalArgumentException("Note not found")

        val updated =
            note.copy(
                title = request.title,
                content = request.content,
                expiresAt = request.expiresAt
            )

        return noteRepository.save(updated).toDto()
    }

    @CacheEvict(value = ["notes", "paginatedNotes"], key = "#root.target.getCurrentUser().username", allEntries = true)
    fun deleteNote(id: Long) {
        val user = getCurrentUser()
        val note =
            noteRepository.findValidNoteByIdAndUser(id, user, now())
                ?: throw IllegalArgumentException("Note not found")

        noteRepository.delete(note)
    }

    private fun Note.toDto() = NoteResponse(id, title, content, createdAt, expiresAt)
}
