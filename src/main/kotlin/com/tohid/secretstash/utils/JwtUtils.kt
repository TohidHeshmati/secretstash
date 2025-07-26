package com.tohid.secretstash.utils

import com.tohid.secretstash.config.JwtProperties
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtUtils(
    private val jwtProperties: JwtProperties
) {
    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())
    }

    fun generateToken(username: String): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtProperties.expirationMs)

        return Jwts
            .builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getUsernameFromToken(token: String): String {
        val claims =
            Jwts
                .parserBuilder()
                .setSigningKey(jwtProperties.secret.toByteArray(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)

        return claims.body.subject
    }
}
