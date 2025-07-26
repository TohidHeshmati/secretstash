package com.tohid.secretstash

import com.tohid.secretstash.domain.User
import com.tohid.secretstash.repository.NoteRepository
import com.tohid.secretstash.repository.UserRepository
import com.tohid.secretstash.service.NoteService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.cache.CacheManager
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseIntegrationTest {
    @Autowired
    protected lateinit var userRepository: UserRepository

    @Autowired
    protected lateinit var noteRepository: NoteRepository

    @Autowired
    protected lateinit var restTemplate: TestRestTemplate

    @Autowired
    protected lateinit var cacheManager: CacheManager

    @Autowired
    protected lateinit var noteService: NoteService

    val headers: HttpHeaders =
        HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

    @LocalServerPort
    private var port: Int = 0

    protected lateinit var baseUrl: String
    protected lateinit var registerEndpoint: String
    protected lateinit var loginEndpoint: String

    @BeforeEach
    fun cleanup() {
        baseUrl = "http://localhost:$port"
        println("Base URL for tests: $baseUrl")
        registerEndpoint = "$baseUrl/v1/auth/register"
        loginEndpoint = "$baseUrl/v1/auth/login"
        noteRepository.deleteAll()
        userRepository.deleteAll()
        val user = userRepository.save(User(username = "cacher", password = "pass"))
        val auth = UsernamePasswordAuthenticationToken(user.username, null, emptyList())
        SecurityContextHolder.getContext().authentication = auth
        println("note and user repositories cleared")
    }

    @AfterEach
    fun clearSecurityContext() {
        SecurityContextHolder.clearContext()
    }
}
