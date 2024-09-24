package com.example.jwtauth.vo

import java.util.UUID

data class Member(
    val name: String,
    val password: String,
    val authority: String,
    val loginId: String,
)

@JvmInline
value class MemberId(val value: UUID)