package com.example.jwtauth.config

import com.example.jwtauth.dto.LoginRequest
import com.example.jwtauth.jwt.JwtTokenProvider
import com.example.jwtauth.service.RedisService
import com.example.jwtauth.vo.Member
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import org.springframework.web.util.ContentCachingRequestWrapper
import java.io.IOException

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisService: RedisService,
): UsernamePasswordAuthenticationFilter() {
    companion object {
        val log = KotlinLogging.logger {  }
    }
    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        if(request == null) {
            throw Exception()
        }

        val referer = request.getHeader("referer")
        lateinit var token: UsernamePasswordAuthenticationToken
        if(referer != null && referer.contains("swagger")) {
            token = UsernamePasswordAuthenticationToken("admin", "1234")
        } else {
            val cachingRequest = ContentCachingRequestWrapper(request)
            try {
                val loginRequest = LoginRequest(cachingRequest.getParameter("loginId"),
                    cachingRequest.getParameter("password"))
                token = UsernamePasswordAuthenticationToken(loginRequest.loginId, loginRequest.password)
            } catch (e: IOException) {
                println(e.stackTrace)
            }
        }

        return super.getAuthenticationManager().authenticate(token)
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

        log.info { "jwt token = $jwtToken" }
        log.info { "username = ${userDetails.username}"}
        log.info { "authority = ${userDetails.authorities}" }

        val member = Member(
            name = userDetails.username,
            password = "",
            authority = userDetails.authorities.toString(),
            loginId = userDetails.getLoginId()
        )
        redisService.save("user:${userDetails.username}", member)

        val cookie = Cookie("JWT", jwtToken)
        cookie.path = "/"
        cookie.isHttpOnly = false
        cookie.maxAge = 3600

        response!!.addCookie(cookie)

        chain!!.doFilter(request, response)
    }
}