package com.example.jwtauth.dto


data class GuitarTxRequest(
    val guitarName: String,
    val txTime: String,
    val county: String,
    val price: Long
)