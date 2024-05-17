package com.example.jwtauth.dto

data class MemberRequest(
    val loginId: String,
    val password: String,
    val name: String,
    val role: String,
)