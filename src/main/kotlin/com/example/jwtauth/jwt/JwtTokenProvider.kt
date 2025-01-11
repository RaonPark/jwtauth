package com.example.jwtauth.jwt

import com.example.jwtauth.config.CustomUserDetails
import com.example.jwtauth.config.CustomUserDetailsService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class JwtTokenProvider(
    val customUserDetailsService: CustomUserDetailsService
) {
    companion object {
        private const val SECRET_KEY = "KINGSGAMBITVIENNAGAMBITSCANDINAVIANDEFENSEITALIANGAMECAROKANNDEFENSE"
        private val key = Keys.hmacShaKeyFor(SECRET_KEY.toByteArray(Charsets.UTF_8))
    }

    fun generateToken(username: String, authorities: MutableCollection<out GrantedAuthority>): String {
        val date = Date()
        val expirationTime = Date(date.time + Duration.ofMinutes(60).toMillis())

        return Jwts.builder()
            .header().type("JWT")
            .and()
            .issuer("raonpark")
            .issuedAt(date)
            .expiration(expirationTime)
            .claim("username", username)
            .claim("authorities", authorities)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        val claims = Jwts.parser().verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload

        return claims.expiration.after(Date(System.currentTimeMillis()))
    }

    fun getUserDetails(token: String): UserDetails {
        val claims = Jwts.parser()
            .verifyWith(key)
            .requireIssuer("raonpark")
            .build()
            .parseSignedClaims(token)
            .payload

        val username = claims["username", String::class.java]

        return customUserDetailsService.loadUserByUsername(username)
    }
}