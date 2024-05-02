package com.example.jwtauth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {
    @Bean
    fun corsConfiguration(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowedMethods = mutableListOf("GET", "POST", "DELETE", "OPTIONS")
        config.allowedHeaders = mutableListOf("Authorization", "referer")
        config.allowedOrigins = mutableListOf("http://localhost:8080")
        config.maxAge = 10000L

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }
}