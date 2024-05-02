package com.example.jwtauth.jwt

import io.jsonwebtoken.Header
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.Date

@Component
class JwtTokenProvider {
    companion object {
        private const val SECRET_KEY = "KINGSGAMBITVIENNAGAMBITSCANDINAVIANDEFENSEITALIANGAMECAROKANNDEFENSE"
        private val key = Keys.hmacShaKeyFor(SECRET_KEY.toByteArray(Charsets.UTF_8))
    }

    fun generateToken(id: String): String {
        val date = Date()
        val expirationTime = Date(date.time + Duration.ofMinutes(60).toMillis())

        return Jwts.builder()
            .header().type("JWT")
            .and()
            .issuer("raonpark")
            .issuedAt(date)
            .expiration(expirationTime)
            .claim("id", id)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String, id: String): Boolean {
        val claims = Jwts.parser().verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload

        return claims.get("id", String::class.java) == id
    }
}