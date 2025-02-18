package com.example.jwtauth.service

import com.example.jwtauth.table.BoardTable
import com.example.jwtauth.table.MemberTable
import com.example.jwtauth.table.toMemberVO
import com.example.jwtauth.vo.Board
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BoardService(
    private val boardTable: BoardTable,
    private val memberTable: MemberTable) {
    fun publishArticle(board: Board): Int {
        val user = transaction {
            memberTable.selectAll().where { MemberTable.name eq board.username }.singleOrNull()?.let {
                it[MemberTable.id]
            }
        }
        return transaction {
            boardTable.insertAndGetId {
                it[title] = board.title
                it[content] = board.content
                it[type] = board.type
                it[published] = board.published
                it[image] = board.image
                it[username] = user!!
            }.value
        }
    }

    fun deleteArticle(boardId: Int): Int {
        return transaction {
            boardTable.deleteWhere { BoardTable.id.eq(boardId) }
        }
    }
}