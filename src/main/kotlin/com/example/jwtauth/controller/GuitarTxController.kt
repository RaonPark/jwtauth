package com.example.jwtauth.controller

import com.example.jwtauth.dto.GuitarTxRequest
import com.example.jwtauth.service.GuitarTxService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/guitarTx")
class GuitarTxController(
    private val guitarTxService: GuitarTxService
) {
    companion object {
        val log = KotlinLogging.logger {  }
    }
    @PostMapping("/tx")
    fun transaction(@RequestBody request: GuitarTxRequest) {
        log.info { "here is guitarTx/tx called" }
        guitarTxService.sendGuitarTx(request)
    }

    @PostMapping("/txDB")
    fun transactionToDB(@RequestBody request: GuitarTxRequest) {
        val guitarTxId = guitarTxService.sendGuitarTxWithDB(request)

        guitarTxService.showGuitarTxRequest(guitarTxId)
    }
}