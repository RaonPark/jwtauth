package com.example.jwtauth.dto

data class LoginRequest(
    val loginId: String,
    val password: String
)