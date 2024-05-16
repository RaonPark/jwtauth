package com.example.jwtauth.config

import com.example.jwtauth.jwt.JwtTokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.filter.OncePerRequestFilter

class JwtRequestFilter(
    private val jwtTokenProvider: JwtTokenProvider
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        RequestPermitURL.entries.map {
            if(request.requestURI == it.url) {
                filterChain.doFilter(request, response)
                return
            }
        }

        val referer = request.getHeader("referer")
        if(referer != null && referer.contains("swagger-ui")) {
            val authority = SimpleGrantedAuthority("ROOT")
            val authorities = mutableListOf(authority)

            setAuthentication("admin", authorities)
            filterChain.doFilter(request, response)
            return
        }

        val cookies = request.cookies
        if(cookies == null) {
            filterChain.doFilter(request, response)
            return
        }

        cookies.forEach {
            if(it.name == "JWT") {
                val token = it.value

                if(jwtTokenProvider.validateToken(token)) {
                    val member = jwtTokenProvider.getUserDetails(token)
                    SecurityContextHolder.getContext().authentication = setAuthentication(member)
                }
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun setAuthentication(username: String, authorities: MutableCollection<out GrantedAuthority>) {

    }

    private fun setAuthentication(userDetails: UserDetails): Authentication {
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }
}