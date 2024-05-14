package com.example.jwtauth.entity

import java.util.UUID

data class Member(
    val id: MemberId,
    val name: String,
    val password: String,
    val authority: String,
    val loginId: String,
)

@JvmInline
value class MemberId(val value: UUID)