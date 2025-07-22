package com.tohid.secretstash

import com.tohid.secretstash.repository.NoteRepository
import com.tohid.secretstash.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseIntegrationTest {

    @Autowired
    protected lateinit var userRepository: UserRepository

    @Autowired
    protected lateinit var noteRepository: NoteRepository


    @LocalServerPort private var port: Int = 0

    protected lateinit var baseUrl: String

    @BeforeEach
    fun cleanup() {
        baseUrl = "http://localhost:$port"
        println("Base URL for tests: $baseUrl")
        noteRepository.deleteAll()
        userRepository.deleteAll()
        println("note and user repositories cleared")

    }
}