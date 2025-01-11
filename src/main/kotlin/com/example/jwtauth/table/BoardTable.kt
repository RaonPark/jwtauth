package com.example.jwtauth.table

import com.example.jwtauth.support.TypeOfBoard
import com.example.jwtauth.vo.Board
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.springframework.stereotype.Component

@Component
object BoardTable: IntIdTable() {
    val title = varchar("title", length = 50)
    val content = varchar("content", length = 1000)
    val image = varchar("image", length = 50)
    val username = reference("username", MemberTable)
    val published = datetime("published").defaultExpression(CurrentDateTime)
//    val type = customEnumeration(name = "type",
//        fromDb = { value -> TypeOfBoard.fromType(value as String)!! }, toDb = { it.type })
    val type = enumerationByName<TypeOfBoard>(name = "type", length = 30)
}

fun ResultRow.toBoardVOJoinMemberTable(): Board = Board(
    title = this[BoardTable.title],
    content = this[BoardTable.content],
    image = this[BoardTable.image],
    username = this[MemberTable.name],
    published = this[BoardTable.published],
    type = TypeOfBoard.fromType(this[BoardTable.type].type)!!
)