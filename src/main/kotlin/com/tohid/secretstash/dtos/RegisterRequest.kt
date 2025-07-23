package com.tohid.secretstash.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:NotBlank(message = "Username is required")
    val username: String,
    @field:Size(min = 6, message = "Password must be at least 6 characters")
    val password: String
    // TODO: add confirmation password field
)
