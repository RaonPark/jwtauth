package com.example.jwtauth.dto

import java.util.UUID

data class MemberResponse(
    val id: UUID,
    val loginId: String,
    val name: String,
    val authority: String,
)