package com.tohid.secretstash.exceptions

data class UserNameAlreadyExistsException(
    override val message: String = "Username already exists"
) : RuntimeException(message)

data class UnAuthorizedException(
    override val message: String = "Unauthorized action"
) : RuntimeException(message)

data class NoAuthenticationException(
    override val message: String = "No authentication found in context"
) : RuntimeException(message)

data class InvalidPrincipalException(
    override val message: String = "Invalid principal in authentication"
) : RuntimeException(message)

data class UserNotFoundException(
    override val message: String = "User not found"
) : RuntimeException(message)
