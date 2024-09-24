package com.example.jwtauth.dto

import java.util.UUID

data class MemberResponse(
    val loginId: String,
    val name: String,
    val authority: String,
)