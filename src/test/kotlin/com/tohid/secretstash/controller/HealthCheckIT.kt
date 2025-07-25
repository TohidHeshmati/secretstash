package com.tohid.secretstash.controller

import com.tohid.secretstash.BaseIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class HealthCheckIT : BaseIntegrationTest() {

    @Test
    fun `should access health endpoint without authentication`() {
        val response = restTemplate.exchange(
            "$baseUrl/actuator/health",
            HttpMethod.GET,
            HttpEntity<Void>(headers),
            String::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).contains("status")
    }

    @Test
    fun `should access liveness probe without authentication`() {
        val response = restTemplate.exchange(
            "$baseUrl/actuator/health/liveness",
            HttpMethod.GET,
            HttpEntity<Void>(headers),
            String::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).contains("status")
    }

    @Test
    fun `should access readiness probe without authentication`() {
        val response = restTemplate.exchange(
            "$baseUrl/actuator/health/readiness",
            HttpMethod.GET,
            HttpEntity<Void>(headers),
            String::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).contains("status")
        // Should contain database and redis components
        assertThat(response.body).contains("db")
        assertThat(response.body).contains("redis")
    }
}
