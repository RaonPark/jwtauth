package com.example.jwtauth.vo

import com.example.jwtauth.dto.GuitarTxRequest
import com.example.jwtauth.support.KindOfTime


data class GuitarTx(
    var guitarName: String,
    var txTime: KindOfTime,
    var price: Long,
    var county: String,
)