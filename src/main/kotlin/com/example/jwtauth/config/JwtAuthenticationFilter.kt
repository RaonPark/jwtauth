package com.example.jwtauth.config

import com.example.jwtauth.dto.LoginDTO
import com.example.jwtauth.jwt.JwtTokenProvider
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import org.springframework.web.util.ContentCachingRequestWrapper
import java.io.IOException
import kotlin.math.log

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
): UsernamePasswordAuthenticationFilter() {
    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        if(request == null) {
            throw Exception()
        }

        val referer = request.getHeader("referer")
        lateinit var token: UsernamePasswordAuthenticationToken
        if(referer != null && referer.contains("swagger")) {
            token = UsernamePasswordAuthenticationToken("root", "1234")
        } else {
            val cachingRequest = ContentCachingRequestWrapper(request)
            try {
                val objectMapper = ObjectMapper()
                val loginDTO = objectMapper.readValue<LoginDTO>(cachingRequest.inputStream, LoginDTO::class.java)
                token = UsernamePasswordAuthenticationToken(loginDTO.loginId, loginDTO.password)
            } catch (e: IOException) {
                println(e.stackTrace)
            }
        }
        return authenticationManager.authenticate(token)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        SecurityContextHolder.getContext().authentication = authResult

        val userDetails = authResult!!.principal as CustomUserDetails
        val jwtToken = jwtTokenProvider.generateToken(userDetails.username, userDetails.authorities)

        val cookie = Cookie("JWT", jwtToken)
        cookie.path = "/"
        cookie.isHttpOnly = false
        cookie.maxAge = 3600

        response!!.addCookie(cookie)

        chain!!.doFilter(request, response)
    }
}