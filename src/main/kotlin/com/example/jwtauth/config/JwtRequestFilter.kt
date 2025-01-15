package com.example.jwtauth.config

import com.example.jwtauth.jwt.JwtTokenProvider
import com.example.jwtauth.service.RedisService
import com.example.jwtauth.table.MemberTable
import com.example.jwtauth.table.toMemberVO
import com.example.jwtauth.vo.Member
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.jetbrains.exposed.sql.selectAll
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtRequestFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberTable: MemberTable,
    private val redisService: RedisService
): OncePerRequestFilter() {
    companion object {
        val log = KotlinLogging.logger {  }
    }
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
            val authority = SimpleGrantedAuthority("ROLE_ROOT")
            val authorities = mutableListOf(authority)

            setAdminAuthentication(authorities)
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
                    log.info { "The JWT token is valid... now validating userDetails" }
                    val member = jwtTokenProvider.getUserDetails(token)
                    if(!isValidMember(member)) {
                        throw RuntimeException("cannot find member ${member.username}")
                    }
                    log.info { "UserDetails valid!" }
                    SecurityContextHolder.getContext().authentication = setAuthentication(userDetails = member)
                    log.info { "here's auth = ${SecurityContextHolder.getContext().authentication}" }
                }
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun isValidMember(userDetails: CustomUserDetails): Boolean {
        val member = redisService.find("user:${userDetails.username}")
        return member != null
    }

    private fun setAdminAuthentication(authorities: MutableCollection<out GrantedAuthority>): Authentication {
        return UsernamePasswordAuthenticationToken("admin", "", authorities)
    }

    private fun setAuthentication(userDetails: CustomUserDetails): Authentication {
        return UsernamePasswordAuthenticationToken(userDetails.username, "", userDetails.authorities)
    }
}