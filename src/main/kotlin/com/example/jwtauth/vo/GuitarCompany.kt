package com.example.jwtauth.vo

import com.example.jwtauth.entity.GuitarEntity
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.SizedIterable

data class GuitarCompany(
    val id: Int,
    val companyName: String,
    val established: LocalDateTime
)