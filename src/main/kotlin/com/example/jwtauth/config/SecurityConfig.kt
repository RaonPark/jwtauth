package com.example.jwtauth.config

import com.example.jwtauth.jwt.JwtTokenProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationConfig: AuthenticationConfiguration
) {
    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity {
            cors {  }
            csrf { disable() }
            formLogin { disable() }
            httpBasic { disable() }
            authorizeRequests {
                authorize("/member/login/**", permitAll)
                authorize("/member/register/**", permitAll)
                authorize("/member/jwt", permitAll)
                authorize(PathRequest.toH2Console(), permitAll)
                authorize("/api/**", hasAnyRole("USER", "BUSINESS", "ADMIN"))
                authorize("/business/**", hasAnyRole("BUSINESS", "ADMIN"))
                authorize("/root/**", hasAnyRole("ADMIN"))
                authorize(anyRequest, authenticated)
            }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }

            addFilterBefore<JwtAuthenticationFilter>(jwtAuthenticationFilter())
        }

        httpSecurity.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter::class.java)

        return httpSecurity.build()
    }

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        val filter = JwtAuthenticationFilter(jwtTokenProvider)
        filter.setAuthenticationManager(authenticationManager(authenticationConfig))
        filter.setFilterProcessesUrl("/member/login")
        return filter
    }

    @Bean
    fun jwtRequestFilter(): JwtRequestFilter {
        return JwtRequestFilter(jwtTokenProvider)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager = config.authenticationManager

    @Bean
    @ConditionalOnProperty(name = ["spring.h2.console.enabled"], havingValue = "true")
    fun configureH2ConsoleEnable(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web -> web.ignoring().requestMatchers(PathRequest.toH2Console()) }
    }
}