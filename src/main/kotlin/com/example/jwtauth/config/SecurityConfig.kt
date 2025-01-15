package com.example.jwtauth.config

import com.example.jwtauth.jwt.JwtTokenProvider
import com.example.jwtauth.service.RedisService
import io.jsonwebtoken.Jwt
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authenticationConfig: AuthenticationConfiguration,
) {
    companion object {
        @Bean
        fun roleHierarchy(): RoleHierarchy {
            return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("ADMIN").implies("BUSINESS")
                .role("BUSINESS").implies("USER")
                .build()
        }

        @Bean
        fun methodSecurityExpressionHandler(): MethodSecurityExpressionHandler {
            val handler = DefaultMethodSecurityExpressionHandler()
            handler.setRoleHierarchy(roleHierarchy())
            return handler
        }
    }

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity,
                            jwtAuthenticationFilter: JwtAuthenticationFilter,
                            jwtRequestFilter: JwtRequestFilter): SecurityFilterChain {
        httpSecurity {
            cors {  }
            csrf { disable() }
            formLogin { disable() }
            httpBasic { disable() }
            authorizeHttpRequests {
                authorize("/member/login/**", permitAll)
                authorize("/member/register/**", permitAll)
                authorize("/member/jwt", permitAll)
                authorize(PathRequest.toH2Console(), permitAll)
                authorize("/member/**", hasAnyRole("USER", "BUSINESS", "ADMIN"))
                authorize("/guitar/**", hasAnyRole("USER", "BUSINESS", "ADMIN"))
                authorize("/guitarTx/**", hasAnyRole("USER", "BUSINESS", "ADMIN"))
                authorize("/business/**", hasAnyRole("BUSINESS", "ADMIN"))
                authorize("/root/**", hasAnyRole("ADMIN"))
                authorize(anyRequest, authenticated)
            }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }

            logout {
                logoutUrl = "/member/logout"
                addLogoutHandler(CookieClearingLogoutHandler("JWT")) // 쿠키를 지우고
                addLogoutHandler(SecurityContextLogoutHandler()) // 시큐리티 컨텍스트를 지운다.
                // logout 이후에 redirection을 하지 않는다.
                logoutSuccessHandler = HttpStatusReturningLogoutSuccessHandler()
            }

            addFilterAt<UsernamePasswordAuthenticationFilter>(jwtAuthenticationFilter)

            authenticationManager = authenticationManager(userDetailsService())
        }

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)

        return httpSecurity.build()
    }

    @Bean
    fun authenticationManager(userDetailsService: UserDetailsService): AuthenticationManager {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailsService)
        authenticationProvider.setPasswordEncoder(passwordEncoder())

        val providerManager = ProviderManager(authenticationProvider)
        // 알아서 credential을 가려준다.
        providerManager.isEraseCredentialsAfterAuthentication = false

        return providerManager
    }

    @Bean
    fun jwtAuthenticationFilter(jwtTokenProvider: JwtTokenProvider, redisService: RedisService): JwtAuthenticationFilter {
        val filter = JwtAuthenticationFilter(jwtTokenProvider, redisService)
        filter.setFilterProcessesUrl("/member/login")
        filter.setAuthenticationManager(authenticationManager(userDetailsService()))
        return filter
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        return CustomUserDetailsService()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @ConditionalOnProperty(name = ["spring.h2.console.enabled"], havingValue = "true")
    fun configureH2ConsoleEnable(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web -> web.ignoring().requestMatchers(PathRequest.toH2Console()) }
    }
}