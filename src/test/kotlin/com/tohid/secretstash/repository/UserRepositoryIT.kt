package com.tohid.secretstash.repository

import com.tohid.secretstash.BaseIntegrationTest
import com.tohid.secretstash.domain.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.dao.DataIntegrityViolationException

class UserRepositoryIT : BaseIntegrationTest() {
    @Test
    fun `should return null if user not found`() {
        val result = userRepository.findByUsername("nonexistent")

        assertThat(result).isNull()
    }

    @Test
    fun `should save and retrieve user by username`() {
        val user = userRepository.save(User(username = "alice", password = "pass123"))

        val found = userRepository.findByUsername("alice")

        assertThat(found).isNotNull
        assertThat(found).isEqualTo(user)
    }

    @Test
    fun `should enforce unique constraint on username`() {
        userRepository.save(User(username = "bob", password = "pass"))

        assertThrows<DataIntegrityViolationException> {
            userRepository.save(User(username = "bob", password = "other"))
        }
    }

    @Test
    fun `should update user password`() {
        userRepository.save(User(username = "david", password = "oldpass"))
        val fetched = userRepository.findByUsername("david")!!

        val updated = fetched.copy(password = "newpass")
        userRepository.save(updated)

        val refreshed = userRepository.findByUsername("david")
        assertThat(refreshed?.password).isEqualTo("newpass")
    }

    @Test
    fun `should delete user by id`() {
        val user = userRepository.save(User(username = "edward", password = "pass"))
        userRepository.deleteById(user.id!!)

        val result = userRepository.findByUsername("edward")
        assertThat(result).isNull()
    }

    @Test
    fun `findByUsername should be case-sensitive`() {
        userRepository.save(User(username = "Frank", password = "secret"))
        val result = userRepository.findByUsername("frank")

        assertThat(result).isNull()
    }
}
