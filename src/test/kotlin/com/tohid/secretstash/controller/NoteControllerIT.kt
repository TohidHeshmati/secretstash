package com.tohid.secretstash.controller

import com.tohid.secretstash.BaseIntegrationTest
import com.tohid.secretstash.dtos.ApiResponse
import com.tohid.secretstash.dtos.AuthResponse
import com.tohid.secretstash.dtos.LoginRequest
import com.tohid.secretstash.dtos.NoteRequest
import com.tohid.secretstash.dtos.NoteResponse
import com.tohid.secretstash.dtos.PagedNoteResponse
import com.tohid.secretstash.dtos.RegisterRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.*

class NoteControllerIT : BaseIntegrationTest() {
    private lateinit var authToken: String

    @BeforeEach
    fun setUpUserAndToken() {
        // Register user
        val registerReq = HttpEntity(RegisterRequest("noter", "secure123"), headers)
        restTemplate.postForEntity(registerEndpoint, registerReq, String::class.java)

        // Login and extract token
        val loginReq = HttpEntity(LoginRequest("noter", "secure123"), headers)
        val loginRes = restTemplate.postForEntity(loginEndpoint, loginReq, AuthResponse::class.java)
        authToken = loginRes.body?.token ?: error("Token not received")
    }

    private fun authorizedHeaders(): HttpHeaders =
        HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            setBearerAuth(authToken)
        }

    @Test
    fun `should create a note`() {
        val noteRequest = NoteRequest("Test Title", "Test Content")
        val request = HttpEntity(noteRequest, authorizedHeaders())

        println("Using token: $authToken")
        val response =
            restTemplate.postForEntity(
                "$baseUrl/v1/notes",
                request,
                NoteResponse::class.java
            )

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body?.title).isEqualTo("Test Title")
    }

    @Test
    fun `should get my notes`() {
        // Create one note
        val noteRequest = NoteRequest("Note 1", "Body", null)
        restTemplate.postForEntity(
            "$baseUrl/v1/notes",
            HttpEntity(noteRequest, authorizedHeaders()),
            NoteResponse::class.java
        )

        val response =
            restTemplate.exchange(
                "$baseUrl/v1/notes",
                HttpMethod.GET,
                HttpEntity<Void>(authorizedHeaders()),
                Array<NoteResponse>::class.java
            )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.size).isGreaterThanOrEqualTo(1)
    }

    @Test
    fun `should get my notes paginated`() {
        for (i in 1..5) {
            val noteRequest = NoteRequest("Paginated Note $i", "Content $i", null)
            restTemplate.postForEntity(
                "$baseUrl/v1/notes",
                HttpEntity(noteRequest, authorizedHeaders()),
                NoteResponse::class.java
            )
        }

        val response =
            restTemplate.exchange(
                "$baseUrl/v1/notes/paginated",
                HttpMethod.GET,
                HttpEntity<Void>(authorizedHeaders()),
                PagedNoteResponse::class.java
            )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.content?.size).isGreaterThanOrEqualTo(5)
        assertThat(response.body?.totalElements).isGreaterThanOrEqualTo(5)

        // Test with custom parameters
        val responseWithParams =
            restTemplate.exchange(
                "$baseUrl/v1/notes/paginated?page=0&size=2&sortBy=createdAt&direction=desc",
                HttpMethod.GET,
                HttpEntity<Void>(authorizedHeaders()),
                PagedNoteResponse::class.java
            )

        assertThat(responseWithParams.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(responseWithParams.body?.content?.size).isEqualTo(2)
        assertThat(responseWithParams.body?.size).isEqualTo(2)
        assertThat(responseWithParams.body?.first).isTrue()
    }

    @Test
    fun `should get note by id`() {
        val noteRequest = NoteRequest("Get Note", "Content", null)
        val postRes =
            restTemplate.postForEntity(
                "$baseUrl/v1/notes",
                HttpEntity(noteRequest, authorizedHeaders()),
                NoteResponse::class.java
            )

        val id = postRes.body?.id!!
        val getRes =
            restTemplate.exchange(
                "$baseUrl/v1/notes/$id",
                HttpMethod.GET,
                HttpEntity<Void>(authorizedHeaders()),
                NoteResponse::class.java
            )

        assertThat(getRes.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(getRes.body?.title).isEqualTo("Get Note")
    }

    @Test
    fun `should update a note`() {
        val created =
            restTemplate.postForEntity(
                "$baseUrl/v1/notes",
                HttpEntity(NoteRequest("Old Title", "Old Content", null), authorizedHeaders()),
                NoteResponse::class.java
            )

        val updateRequest = NoteRequest("Updated Title", "Updated Content")
        val updated =
            restTemplate.exchange(
                "$baseUrl/v1/notes/${created.body?.id}",
                HttpMethod.PUT,
                HttpEntity(updateRequest, authorizedHeaders()),
                NoteResponse::class.java
            )

        assertThat(updated.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(updated.body?.title).isEqualTo("Updated Title")
    }

    @Test
    fun `should delete a note`() {
        val created =
            restTemplate.postForEntity(
                "$baseUrl/v1/notes",
                HttpEntity(NoteRequest("Delete Me", "Bye", null), authorizedHeaders()),
                NoteResponse::class.java
            )

        val deleteRes =
            restTemplate.exchange(
                "$baseUrl/v1/notes/${created.body?.id}",
                HttpMethod.DELETE,
                HttpEntity<Void>(authorizedHeaders()),
                Void::class.java
            )

        assertThat(deleteRes.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @Test
    fun `should return 401 when accessing notes without auth`() {
        val response =
            restTemplate.exchange(
                "$baseUrl/v1/notes",
                HttpMethod.GET,
                HttpEntity<Void>(headers),
                String::class.java
            )

        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun `should return 404 when note is not found`() {
        // Use a non-existent note ID
        val nonExistentId = 999999L

        val response = restTemplate.exchange(
            "$baseUrl/v1/notes/$nonExistentId",
            HttpMethod.GET,
            HttpEntity<Void>(authorizedHeaders()),
            ApiResponse::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body?.message).isEqualTo("Note not found")
    }
}
