package com.tohid.secretstash.controller

import com.tohid.secretstash.dtos.ApiResponse
import com.tohid.secretstash.dtos.LoginRequest
import com.tohid.secretstash.dtos.RegisterRequest
import com.tohid.secretstash.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<ApiResponse> {
        return try {
            val response = authService.registerUser(request)
            ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(ApiResponse(e.message ?: "Invalid input"))
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Any> {
        return try {
            val token = authService.loginUser(request)
            ResponseEntity.ok(token)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(401).body(ApiResponse(e.message ?: "Unauthorized"))
        }
    }
}