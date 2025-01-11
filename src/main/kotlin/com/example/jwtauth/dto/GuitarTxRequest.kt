package com.example.jwtauth.dto

import kotlinx.datetime.LocalDateTime


data class GuitarTxRequest(
    val guitarName: String,
    val txTime: LocalDateTime,
    val county: String,
    val price: Long
)