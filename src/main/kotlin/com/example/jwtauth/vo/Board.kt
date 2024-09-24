package com.example.jwtauth.vo

import com.example.jwtauth.support.TypeOfBoard
import kotlinx.datetime.LocalDateTime

data class Board(
    val title: String,
    val content: String,
    val image: String,
    val username: String,
    val published: LocalDateTime,
    val type: TypeOfBoard,
)