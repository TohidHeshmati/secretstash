package com.tohid.secretstash.service

import com.tohid.secretstash.domain.User
import com.tohid.secretstash.dtos.ApiResponse
import com.tohid.secretstash.dtos.AuthResponse
import com.tohid.secretstash.dtos.LoginRequest
import com.tohid.secretstash.dtos.RegisterRequest
import com.tohid.secretstash.exceptions.UnAuthorizedException
import com.tohid.secretstash.exceptions.UserNameAlreadyExistsException
import com.tohid.secretstash.repository.UserRepository
import com.tohid.secretstash.utils.JwtUtils
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtils: JwtUtils
) {
    fun registerUser(request: RegisterRequest): ApiResponse {
        if (userRepository.findByUsername(request.username) != null) {
            throw UserNameAlreadyExistsException("Username '${request.username}' already exists")
        }

        val encodedPassword = passwordEncoder.encode(request.password)
        val newUser = User(username = request.username, password = encodedPassword)
        userRepository.save(newUser)

        return ApiResponse("User registered successfully")
    }

    fun loginUser(request: LoginRequest): AuthResponse {
        val user = userRepository.findByUsername(request.username) ?: invalidCredentials()
        if (!passwordEncoder.matches(request.password, user.password)) invalidCredentials()

        val token = jwtUtils.generateToken(user.username)
        return AuthResponse(token)
    }

    fun invalidCredentials(): Nothing = throw UnAuthorizedException("Invalid credentials")
}
