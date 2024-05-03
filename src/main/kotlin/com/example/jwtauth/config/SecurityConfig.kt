package com.example.jwtauth.config

import com.example.jwtauth.jwt.JwtTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider
) {
    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity {
            cors {  }
            csrf { disable() }
            formLogin { disable() }
            httpBasic { disable() }
            authorizeRequests {
                authorize("/api/login/**", permitAll)
                authorize("/api/register/**", permitAll)
                authorize("/api/**", hasAnyRole("USER", "BUSINESS", "ADMIN"))
                authorize("/business/**", hasAnyRole("BUSINESS", "ADMIN"))
                authorize("/root/**", hasAnyRole("ADMIN"))
                authorize(anyRequest, authenticated)
            }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }

            addFilterBefore<JwtAuthenticationFilter>(jwtAuthenticationFilter())
        }

        return httpSecurity.build()
    }

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        val filter = JwtAuthenticationFilter(jwtTokenProvider)
        filter.setFilterProcessesUrl("/api/login")
        return filter
    }
}