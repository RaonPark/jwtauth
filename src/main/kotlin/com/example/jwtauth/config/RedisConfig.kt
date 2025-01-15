package com.example.jwtauth.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@EnableConfigurationProperties(RedisConfig.RedisConfigurationValues::class)
class RedisConfig {
    @ConfigurationProperties(prefix = "spring.data.redis")
    data class RedisConfigurationValues (
        val host: String,
        val port: Int
    )


    @Bean
    fun redisConnectionFactory(redisConfigurationValues: RedisConfigurationValues): LettuceConnectionFactory {
        return LettuceConnectionFactory(
            RedisStandaloneConfiguration(redisConfigurationValues.host, redisConfigurationValues.port))
    }

    @Bean
    fun redisTemplateForJson(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.connectionFactory = redisConnectionFactory
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(Any::class.java)
        return redisTemplate
    }
}