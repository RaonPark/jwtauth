package com.example.jwtauth.dto

import kotlinx.datetime.LocalDateTime

data class CreateGuitarCompanyRequest(
    val companyName: String,
    val established: LocalDateTime
)