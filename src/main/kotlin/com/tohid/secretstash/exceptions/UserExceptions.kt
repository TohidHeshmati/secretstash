package com.tohid.secretstash.exceptions

data class UserNameAlreadyExistsException(
    override val message: String = "Username already exists"
) : RuntimeException(message)


data class UnAuthorizedException(
    override val message: String = "Unauthorized action"
) : RuntimeException(message)