package com.example.jwtauth.config.kafka

import com.example.jwtauth.vo.GuitarTx
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
class KafkaConsumerConfig {
    val configMap = mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "kafka1:9092, kafka2:9092",
        ConsumerConfig.GROUP_ID_CONFIG to "GTX",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to ErrorHandlingDeserializer::class.java,
        ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS to JsonDeserializer::class.java,
        JsonDeserializer.USE_TYPE_INFO_HEADERS to false,
        JsonDeserializer.TRUSTED_PACKAGES to "*",
        "spring.kafka.consumer.properties.spring.json.encoding" to "UTF-8",
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to false,
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest"
    )

//    @Bean
//    fun <T> kafkaConsumerForGenericJson() = DefaultKafkaConsumerFactory<String, T>(configMap)
//
//    @Bean
//    fun <T> kafkaListenerContainerFactoryForGenericJson(): ConcurrentKafkaListenerContainerFactory<String, T> {
//        val listenerContainer = ConcurrentKafkaListenerContainerFactory<String, T>()
//        listenerContainer.consumerFactory = kafkaConsumerForGenericJson()
//        return listenerContainer
//    }

    @Bean
    fun kafkaConsumerForGuitarTx() = DefaultKafkaConsumerFactory(configMap,
        StringDeserializer(), JsonDeserializer(GuitarTx::class.java, false))

    @Bean
    fun kafkaListenerContainerFactoryForGuitarTx(): ConcurrentKafkaListenerContainerFactory<String, GuitarTx> {
        val listenerContainer = ConcurrentKafkaListenerContainerFactory<String, GuitarTx>()
        listenerContainer.consumerFactory = kafkaConsumerForGuitarTx()
        return listenerContainer
    }
}