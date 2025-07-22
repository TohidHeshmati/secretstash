package com.tohid.secretstash.service

import com.tohid.secretstash.domain.Note
import com.tohid.secretstash.domain.User
import com.tohid.secretstash.dtos.NoteRequest
import com.tohid.secretstash.dtos.NoteResponse
import com.tohid.secretstash.repository.NoteRepository
import com.tohid.secretstash.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class NoteService(
    private val noteRepository: NoteRepository,
    private val userRepository: UserRepository
) {

    fun getCurrentUser(): User {
        val username = SecurityContextHolder.getContext().authentication.principal as String
        return userRepository.findByUsername(username)
            ?: throw IllegalStateException("User not found")
    }

    fun createNote(request: NoteRequest): NoteResponse {
        val user = getCurrentUser()

        val note = Note(
            title = request.title,
            content = request.content,
            expiresAt = request.expiresAt,
            user = user
        )
        return noteRepository.save(note).toDto()
    }

    fun getMyNotes(): List<NoteResponse> {
        val user = getCurrentUser()
        val now = Instant.now()
        return noteRepository
            .findByUserAndExpiresAtAfterOrExpiresAtIsNullOrderByCreatedAtDesc(user, now)
            .take(1000)
            .map { it.toDto() }
    }

    fun getNoteById(id: Long): NoteResponse {
        val user = getCurrentUser()
        val note = noteRepository.findByIdAndUser(id, user)
            ?: throw IllegalArgumentException("Note not found")
        return note.toDto()
    }

    fun updateNote(id: Long, request: NoteRequest): NoteResponse {
        val user = getCurrentUser()
        val note = noteRepository.findByIdAndUser(id, user)
            ?: throw IllegalArgumentException("Note not found")

        val updated = note.copy(
            title = request.title,
            content = request.content,
            expiresAt = request.expiresAt
        )

        return noteRepository.save(updated).toDto()
    }

    fun deleteNote(id: Long) {
        val user = getCurrentUser()
        val note = noteRepository.findByIdAndUser(id, user)
            ?: throw IllegalArgumentException("Note not found")

        noteRepository.delete(note)
    }

    private fun Note.toDto() = NoteResponse(id, title, content, createdAt, expiresAt)
}