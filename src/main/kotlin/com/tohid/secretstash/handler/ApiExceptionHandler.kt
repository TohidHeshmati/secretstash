package com.tohid.secretstash.handler

import com.tohid.secretstash.dtos.ApiResponse
import com.tohid.secretstash.exceptions.NoteNotFoundException
import com.tohid.secretstash.exceptions.UnAuthorizedException
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(UnAuthorizedException::class)
    fun handleUnauthorized(ex: UnAuthorizedException): ResponseEntity<ApiResponse> =
        ResponseEntity.status(401).body(ApiResponse(ex.message))

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ApiResponse> =
        ResponseEntity.badRequest().body(ApiResponse(ex.message ?: "Bad request"))

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthException(ex: AuthenticationException): ResponseEntity<ApiResponse> =
        ResponseEntity.status(401).body(ApiResponse(ex.message ?: "Unauthorized"))

    @ExceptionHandler(NoteNotFoundException::class)
    fun handleNoteNotFound(ex: NoteNotFoundException): ResponseEntity<ApiResponse> =
        ResponseEntity.status(404).body(ApiResponse(ex.message ?: "Note not found"))
}
