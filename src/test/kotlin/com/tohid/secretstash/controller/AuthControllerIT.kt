package com.tohid.secretstash.controller

import com.tohid.secretstash.BaseIntegrationTest
import com.tohid.secretstash.domain.User
import com.tohid.secretstash.dtos.ApiResponse
import com.tohid.secretstash.dtos.LoginRequest
import com.tohid.secretstash.dtos.RegisterRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus

class AuthControllerIT : BaseIntegrationTest() {

    @Test
    fun `should register a new user`() {
        val requestBody = RegisterRequest(username = "testuser", password = "securepass")
        val request = HttpEntity(requestBody, headers)

        val response = restTemplate.postForEntity(registerEndpoint, request, ApiResponse::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.success).isTrue()
        assertThat(userRepository.findByUsername("testuser")).isNotNull()
    }

    @Test
    fun `should not register user with duplicate username`() {
        userRepository.save(User(username = "existing", password = "secret"))

        val requestBody = RegisterRequest(username = "existing", password = "another")
        val request = HttpEntity(requestBody, headers)

        val response = restTemplate.postForEntity(registerEndpoint, request, String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun `should login with correct credentials`() {
        restTemplate.postForEntity(
            registerEndpoint,
            HttpEntity(RegisterRequest("loginuser", "mypassword"), headers),
            ApiResponse::class.java
        )
        val loginRequestBody = LoginRequest(username = "loginuser", password = "mypassword")
        val request = HttpEntity(loginRequestBody, headers)

        val response = restTemplate.postForEntity(loginEndpoint, request, String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isNotBlank()
    }

    @Test
    fun `should fail login with incorrect password`() {
        userRepository.save(User(username = "secureuser", password = "correctpass"))
        val loginRequestBody = LoginRequest(username = "secureuser", password = "wrongpass")
        val request = HttpEntity(loginRequestBody, headers)

        val response = restTemplate.postForEntity(loginEndpoint, request, String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
        assertThat(response.body).contains("Invalid credentials")
    }
}