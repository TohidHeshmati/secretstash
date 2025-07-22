package com.tohid.secretstash.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtUtils(
    @Value("\${app.jwt.secret}") private val secret: String,
    @Value("\${app.jwt.expirationMs}") private val expirationMs: Long
) {

    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(secret.toByteArray())
    }

    fun generateToken(username: String): String {
        val now = Date()
        val expiryDate = Date(now.time + expirationMs)

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getUsernameFromToken(token: String): String {
    val claims = Jwts.parserBuilder()
        .setSigningKey(secret.toByteArray(StandardCharsets.UTF_8))
        .build()
        .parseClaimsJws(token)

    return claims.body.subject
}
}