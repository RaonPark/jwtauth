package com.example.jwtauth.service

import com.example.jwtauth.dto.GuitarTxRequest
import com.example.jwtauth.repository.GuitarTxRepo
import com.example.jwtauth.support.KindOfTime
import com.example.jwtauth.vo.GuitarTx
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class GuitarTxService(
    private val guitarTxRepo: GuitarTxRepo,
    private val kafkaTemplate: KafkaTemplate<String, GuitarTx>,
) {
    companion object {
        var globalFlag = true
        val logger = KotlinLogging.logger {  }
    }
    fun sendGuitarTx(guitarTxRequest: GuitarTxRequest) {
        val guitarTx = GuitarTx(
            price = guitarTxRequest.price,
            guitarName = guitarTxRequest.guitarName,
            txTime = KindOfTime.toKind(guitarTxRequest.txTime),
            county = guitarTxRequest.county
        )
        val txKey = getNextTxKey()
        kafkaTemplate.send("guitarTx", txKey, guitarTx)
    }

    fun getNextTxKey(): String =
        if(globalFlag) {
            globalFlag = false
            "1"
        } else {
            globalFlag = true
            "2"
        }

    @KafkaListener(topics = ["guitarTx"], containerFactory = "kafkaListenerContainerFactoryForGuitarTx")
    fun consumeGuitarTx(record: ConsumerRecord<String, GuitarTx>) {
        val txKey = record.key()
        val guitarTx = record.value()

        logger.info { "txKey = $txKey and tx = ${guitarTx.price}, occurred = ${guitarTx.txTime.name}" }
    }

    fun sendGuitarTxWithDB(guitarTxRequest: GuitarTxRequest): Int {
        val guitarTx = GuitarTx(
            price = guitarTxRequest.price,
            guitarName = guitarTxRequest.guitarName,
            txTime =  KindOfTime.toKind(guitarTxRequest.txTime),
            county = guitarTxRequest.county
        )

        return transaction { guitarTxRepo.insert(guitarTx) }
    }

    fun showGuitarTxRequest(guitarTxId: Int) {
        val guitarTx = transaction { guitarTxRepo.findById(guitarTxId) }

        logger.info { "tx = ${guitarTx.price}, occurred = ${guitarTx.txTime.name}, area = ${guitarTx.county}" }
    }
}