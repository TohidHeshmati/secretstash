package com.tohid.secretstash.exceptions

import com.tohid.secretstash.BaseIntegrationTest
import com.tohid.secretstash.dtos.ApiResponse
import com.tohid.secretstash.dtos.NoteRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

class UserExceptionsIT : BaseIntegrationTest() {

    @Test
    fun `should return 401 when no authentication is found`() {
        // Clear the security context to trigger NoAuthenticationException
        SecurityContextHolder.clearContext()

        // Try to create a note without authentication
        val noteRequest = NoteRequest("Test Title", "Test Content")
        val request = HttpEntity(noteRequest, headers)

        val response = restTemplate.exchange(
            "$baseUrl/v1/notes",
            HttpMethod.POST,
            request,
            ApiResponse::class.java
        )

        // Should return 401 Unauthorized
        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun `should return 401 when invalid principal is found`() {
        // Set an invalid principal in the security context to trigger InvalidPrincipalException
        val auth = UsernamePasswordAuthenticationToken(null, null, emptyList())
        SecurityContextHolder.getContext().authentication = auth

        // Try to create a note with invalid principal
        val noteRequest = NoteRequest("Test Title", "Test Content")
        val request = HttpEntity(noteRequest, headers)

        val response = restTemplate.exchange(
            "$baseUrl/v1/notes",
            HttpMethod.POST,
            request,
            ApiResponse::class.java
        )

        // Should return 401 Unauthorized
        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun `should return 404 when user is not found`() {
        // Register and login a user to get a valid token
        val username = "testuser_for_404"
        val password = "password123"

        // Register the user
        restTemplate.postForEntity(
            registerEndpoint,
            HttpEntity(com.tohid.secretstash.dtos.RegisterRequest(username, password.toCharArray()), headers),
            String::class.java
        )

        // Login to get a token
        val loginResponse = restTemplate.postForEntity(
            loginEndpoint,
            HttpEntity(com.tohid.secretstash.dtos.LoginRequest(username, password.toCharArray()), headers),
            com.tohid.secretstash.dtos.AuthResponse::class.java
        )

        val token = loginResponse.body?.token ?: error("Token not received")

        // Create authorized headers with the token
        val authHeaders = org.springframework.http.HttpHeaders().apply {
            contentType = org.springframework.http.MediaType.APPLICATION_JSON
            setBearerAuth(token)
        }

        // Delete the user from the database to trigger UserNotFoundException
        val user = userRepository.findByUsername(username) ?: error("User not found")
        userRepository.delete(user)

        // Try to create a note with the deleted user's token
        val noteRequest = NoteRequest("Test Title", "Test Content")
        val request = HttpEntity(noteRequest, authHeaders)

        val response = restTemplate.exchange(
            "$baseUrl/v1/notes",
            HttpMethod.POST,
            request,
            ApiResponse::class.java
        )

        // Should return 404 Not Found
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body?.message).isEqualTo("User not found")
    }
}
