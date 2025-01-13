package com.example.jwtauth.controller

import com.example.jwtauth.dto.GuitarTxRequest
import com.example.jwtauth.service.GuitarTxService
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
    @PostMapping("/tx")
    fun transaction(@RequestBody request: GuitarTxRequest) {
        guitarTxService.sendGuitarTx(request)
    }

    @PostMapping("/txDB")
    fun transactionToDB(@RequestBody request: GuitarTxRequest) {
        val guitarTxId = guitarTxService.sendGuitarTxWithDB(request)

        guitarTxService.showGuitarTxRequest(guitarTxId)
    }
}