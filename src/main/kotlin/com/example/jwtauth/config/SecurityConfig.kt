package com.example.jwtauth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {
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
        }

        return httpSecurity.build()
    }
}