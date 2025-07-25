package com.tohid.secretstash.dtos;

import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class RegisterRequestTest {


    companion object {
        lateinit var validator: Validator

        @JvmStatic
        @BeforeAll
        fun setup() {
            val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
            validator = factory.validator
        }
    }

    @Test
    fun `should return validation error for blank username`() {
        val request = RegisterRequest(username = "   ", password = "securePass")

        val violations = validator.validate(request)

        assertThat(1).isEqualTo(violations.size)
        assertEquals("Username is required", violations.first().message)
    }

    @Test
    fun `should return validation error for short password`() {
        val request = RegisterRequest(username = "user", password = "123")

        val violations = validator.validate(request)

        assertEquals(1, violations.size)
        assertEquals("Password must be at least 6 characters", violations.first().message)
    }

    @Test
    fun `should pass validation when input is valid`() {
        val request = RegisterRequest(username = "user", password = "password123")

        val violations = validator.validate(request)

        assertEquals(0, violations.size)
    }
}
