package com.example.jwtauth.config.kafka

import com.example.jwtauth.vo.GuitarTx
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaProducerConfig {
    // ACKS = STRING 이어야한다.
    val configMap = mapOf(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "kafka1:9092,kafka2:9092",
        ProducerConfig.LINGER_MS_CONFIG to "10",
        ProducerConfig.ACKS_CONFIG to "1",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
        JsonSerializer.ADD_TYPE_INFO_HEADERS to false,
        ProducerConfig.COMPRESSION_TYPE_CONFIG to "snappy",
        ProducerConfig.BATCH_SIZE_CONFIG to (32 * 1024).toString()
    )

    @Bean
    fun producerFactoryForGuitarTx() = DefaultKafkaProducerFactory<String, GuitarTx>(configMap)

    @Bean
    fun kafkaTemplateForGuitarTx() = KafkaTemplate(producerFactoryForGuitarTx())
}