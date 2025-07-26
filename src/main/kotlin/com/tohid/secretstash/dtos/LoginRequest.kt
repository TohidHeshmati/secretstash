package com.tohid.secretstash.dtos

import com.tohid.secretstash.validators.NotBlankCharArray
import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "Username is required")
    val username: String,
    @field:NotBlankCharArray(message = "Password is required")
    val password: CharArray
) {
    // Override equals and hashCode to properly compare CharArray contents
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoginRequest

        if (username != other.username) return false
        if (!password.contentEquals(other.password)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + password.contentHashCode()
        return result
    }
}
