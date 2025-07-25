package com.tohid.secretstash.repository

import com.tohid.secretstash.domain.Note
import com.tohid.secretstash.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface NoteRepository : JpaRepository<Note, Long> {
    fun findByUserAndExpiresAtAfterOrExpiresAtIsNullOrderByCreatedAtDesc(
        user: User,
        now: Instant
    ): List<Note>

    fun findByIdAndUser(
        id: Long,
        user: User
    ): Note?
}
