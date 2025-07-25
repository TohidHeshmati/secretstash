package com.tohid.secretstash.repository

import com.tohid.secretstash.domain.Note
import com.tohid.secretstash.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.Instant
import java.time.Instant.now

interface NoteRepository : JpaRepository<Note, Long> {
    fun findByUserAndExpiresAtAfterOrExpiresAtIsNullOrderByCreatedAtDesc(
        user: User,
        now: Instant = now()
    ): List<Note>

    @Query(
        """
    SELECT n FROM Note n
    WHERE n.id = :id AND n.user = :user AND
          (n.expiresAt > :now OR n.expiresAt IS NULL)
    """
    )
    fun findValidNoteByIdAndUser(
        id: Long,
        user: User,
        now: Instant = now()
    ): Note?
}
