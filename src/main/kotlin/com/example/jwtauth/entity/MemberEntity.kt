package com.example.jwtauth.entity

import org.jetbrains.exposed.dao.id.UUIDTable

object MemberEntity: UUIDTable("member", "id") {
    val loginId = varchar("loginId", 30)
    val name = varchar("name", 50)
    val password = varchar("password", 100)
    val authority = varchar("authority", 10)
}