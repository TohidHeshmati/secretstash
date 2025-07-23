package com.tohid.secretstash.controller

import com.tohid.secretstash.dtos.ApiResponse
import com.tohid.secretstash.dtos.LoginRequest
import com.tohid.secretstash.dtos.RegisterRequest
import com.tohid.secretstash.service.AuthService
import jakarta.validation.Valid
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
    fun register(@RequestBody @Valid request: RegisterRequest): ResponseEntity<ApiResponse> {
        val response = authService.registerUser(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Any> {
        val token = authService.loginUser(request)
        return ResponseEntity.ok(token)
    }
}