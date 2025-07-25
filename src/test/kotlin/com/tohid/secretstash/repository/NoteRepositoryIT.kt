package com.tohid.secretstash.repository

import com.tohid.secretstash.BaseIntegrationTest
import com.tohid.secretstash.domain.Note
import com.tohid.secretstash.domain.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

class NoteRepositoryIT : BaseIntegrationTest() {
    @Test
    fun `should find notes for user where expiresAt is null or in future`() {
        val user = userRepository.save(User(username = "alice", password = "pass"))

        val now = Instant.now()
        val future = now.plus(1, ChronoUnit.DAYS)
        val past = now.minus(1, ChronoUnit.DAYS)

        val note1 =
            noteRepository.save(
                Note(title = "Note 1", content = "no expiry", user = user, createdAt = now, expiresAt = null)
            )
        val note2 =
            noteRepository.save(
                Note(
                    title = "Note 2",
                    content = "future expiry",
                    user = user,
                    createdAt = now.plusSeconds(10),
                    expiresAt = future
                )
            )
        val expiredNote =
            noteRepository.save(
                Note(
                    title = "Expired",
                    content = "expired",
                    user = user,
                    createdAt = now.minusSeconds(10),
                    expiresAt = past
                )
            )

        val result = noteRepository.findByUserAndExpiresAtAfterOrExpiresAtIsNullOrderByCreatedAtDesc(user, now)

        assertThat(result).containsExactly(note2, note1)
        assertThat(result).doesNotContain(expiredNote)
    }

    @Test
    fun `should return empty list if user has no matching notes`() {
        val user = userRepository.save(User(username = "bob", password = "pass"))
        val result =
            noteRepository.findByUserAndExpiresAtAfterOrExpiresAtIsNullOrderByCreatedAtDesc(
                user,
                Instant.now()
            )

        assertThat(result).isEmpty()
    }

    @Test
    fun `should find note by id and user`() {
        val user = userRepository.save(User(username = "charlie", password = "pass"))
        val note =
            noteRepository.save(
                Note(
                    title = "Private",
                    content = "Charlie's Note",
                    user = user,
                    createdAt = Instant.now(),
                    expiresAt = null
                )
            )

        val found = noteRepository.findByIdAndUser(note.id!!, user)
        assertThat(found).isNotNull
        assertThat(found?.title).isEqualTo("Private")
    }

    @Test
    fun `should return null if note id does not belong to user`() {
        val user1 = userRepository.save(User(username = "dave", password = "123"))
        val user2 = userRepository.save(User(username = "emma", password = "123"))

        val note =
            noteRepository.save(
                Note(title = "Hidden", content = "Not yours", user = user1, createdAt = Instant.now(), expiresAt = null)
            )

        val found = noteRepository.findByIdAndUser(note.id!!, user2)
        assertThat(found).isNull()
    }

    @Test
    fun `should delete all notes when user is deleted due to cascade`() {
        val user = userRepository.save(User(username = "frank", password = "123"))
        noteRepository.save(
            Note(title = "To delete", content = "Bye", user = user, createdAt = Instant.now(), expiresAt = null)
        )

        userRepository.delete(user)

        val remainingNotes = noteRepository.findAll()
        assertThat(remainingNotes).isEmpty()
    }

    @Test
    fun `should persist note with nullable expiresAt`() {
        val user = userRepository.save(User(username = "grace", password = "123"))

        val note =
            noteRepository.save(
                Note(
                    title = "Timeless",
                    content = "Never expires",
                    user = user,
                    createdAt = Instant.now(),
                    expiresAt = null
                )
            )

        assertThat(note.id).isNotNull
        assertThat(note.expiresAt).isNull()
    }
}
