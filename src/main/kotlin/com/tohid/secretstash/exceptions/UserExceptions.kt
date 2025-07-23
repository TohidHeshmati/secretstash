package com.tohid.secretstash.exceptions

data class UserNameAlreadyExistsException(
    override val message: String = "Username already exists"
) : RuntimeException(message)