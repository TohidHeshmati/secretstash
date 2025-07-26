package com.tohid.secretstash.controller

import com.tohid.secretstash.dtos.ApiResponse
import com.tohid.secretstash.dtos.AuthResponse
import com.tohid.secretstash.dtos.LoginRequest
import com.tohid.secretstash.dtos.RegisterRequest
import com.tohid.secretstash.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "Authentication", description = "Authentication API for user registration and login")
class AuthController(
    private val authService: AuthService
) {
    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account with the provided username and password"
    )
    @ApiResponses(
        value = [
            SwaggerResponse(
                responseCode = "201",
                description = "User registered successfully",
                content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ApiResponse::class))]
            ),
            SwaggerResponse(
                responseCode = "403",
                description = "Username already exists",
                content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)]
            ),
            SwaggerResponse(
                responseCode = "400",
                description = "Invalid input data",
                content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)]
            )
        ]
    )
    @PostMapping("/register")
    fun register(
        @Parameter(description = "User registration details", required = true)
        @RequestBody @Valid request: RegisterRequest
    ): ResponseEntity<ApiResponse> {
        val response = authService.registerUser(request)
        return ResponseEntity.status(201).body(response)
    }

    @Operation(
        summary = "Login user",
        description = "Authenticates a user and returns a JWT token for accessing protected endpoints"
    )
    @ApiResponses(
        value = [
            SwaggerResponse(
                responseCode = "200",
                description = "Authentication successful",
                content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = AuthResponse::class))]
            ),
            SwaggerResponse(
                responseCode = "401",
                description = "Invalid credentials",
                content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)]
            ),
            SwaggerResponse(
                responseCode = "400",
                description = "Invalid input data",
                content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)]
            )
        ]
    )
    @PostMapping("/login")
    fun login(
        @Parameter(description = "User login credentials", required = true)
        @RequestBody @Valid request: LoginRequest
    ): ResponseEntity<AuthResponse> {
        val token = authService.loginUser(request)
        return ResponseEntity.ok(token)
    }
}
