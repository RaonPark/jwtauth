package com.example.jwtauth.table

import com.example.jwtauth.vo.Member
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow

object MemberTable: UUIDTable("member", "id") {
    val loginId = varchar("loginId", 30)
    val name = varchar("name", 50)
    val password = varchar("password", 100)
    val authority = varchar("authority", 10)
}

fun ResultRow.toMemberVO(): Member = Member(
    name = this[MemberTable.name],
    loginId = this[MemberTable.loginId],
    password = this[MemberTable.password],
    authority = this[MemberTable.authority]
)