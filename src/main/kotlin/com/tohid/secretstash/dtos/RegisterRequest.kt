package com.tohid.secretstash.dtos

import com.tohid.secretstash.validators.NotBlankCharArray
import com.tohid.secretstash.validators.SizeCharArray
import jakarta.validation.constraints.NotBlank

data class RegisterRequest(
    @field:NotBlank(message = "Username is required")
    val username: String,
    @field:NotBlankCharArray(message = "Password is required")
    @field:SizeCharArray(min = 6, message = "Password must be at least 6 characters")
    val password: CharArray
    // TODO: add confirmation password field
) {
    // Override equals and hashCode to properly compare CharArray contents
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RegisterRequest

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
