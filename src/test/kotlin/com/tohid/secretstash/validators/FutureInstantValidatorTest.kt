package com.tohid.secretstash.validators

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals

class FutureInstantValidatorTest {
    companion object {
        private lateinit var validator: Validator

        @BeforeAll
        @JvmStatic
        fun setup() {
            validator = Validation.buildDefaultValidatorFactory().validator
        }
    }

    data class TestData(
        @field:FutureInstant
        val date: Instant?
    )

    @Test
    fun `null value should be valid`() {
        val data = TestData(null)
        val violations = validator.validate(data)
        assertThat(violations).isEmpty()
    }

    @Test
    fun `future instant should be valid`() {
        val future = Instant.now().plusSeconds(60)
        val data = TestData(future)
        val violations = validator.validate(data)
        assertThat(violations).isEmpty()
    }

    @Test
    fun `past instant should be invalid`() {
        val past = Instant.now().minusSeconds(60)
        val data = TestData(past)
        val violations = validator.validate(data)

        assertThat(violations).hasSize(1)
        assertEquals("The date must be in the future", violations.first().message)
    }

    @Test
    fun `exactly now should be invalid`() {
        val now = Instant.now()
        val data = TestData(now)
        val violations = validator.validate(data)

        assertThat(violations).hasSize(1)
    }
}
