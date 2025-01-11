package com.example.jwtauth.config.kafka

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.config.TopicConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaAdmin

@Configuration
class KafkaTopicConfig {
    @Bean
    fun admin() = KafkaAdmin(
        mapOf(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to "kafka1:9092,kafka2:9092",
            AdminClientConfig.SECURITY_PROTOCOL_CONFIG to "PLAINTEXT")
    )
    @Bean
    fun transactionTopic(): NewTopic {
        return TopicBuilder.name("guitarTx")
            .replicas(1)
            .partitions(10)
            .config(TopicConfig.COMPRESSION_TYPE_CONFIG, "producer")
            .build()
    }
}