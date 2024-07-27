package com.example.jwtauth.dto

data class CreateGuitarRequest(
    val model: String,
    val price: Int,
    val companyName: String,
    val isUsed: Boolean,
) {
}