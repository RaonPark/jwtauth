package com.example.jwtauth.vo

import com.example.jwtauth.support.TypeOfBoard
import kotlinx.datetime.LocalDateTime

data class Board(
    var title: String,
    var content: String,
    var image: String,
    var username: String,
    var published: LocalDateTime,
    var type: TypeOfBoard,
)