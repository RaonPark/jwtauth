package com.example.jwtauth.support

import kotlinx.datetime.LocalDateTime

enum class KindOfTime(val expr: String) {
    MORNING("오전"),
    AFTERNOON("오후"),
    NIGHT("밤"),
    DAWN("새벽"),
    ;

    companion object {
        fun toKind(time: LocalDateTime): KindOfTime {
            val hour = time.hour
            return when {
                (hour in 6..11) -> MORNING
                (hour in 12..18) -> AFTERNOON
                (hour in 19..22) -> NIGHT
                else -> DAWN
            }
        }
    }
}