package com.example.jwtauth.table

import com.example.jwtauth.support.TypeOfBoard
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(classes = [BoardTable::class])
class BoardTableTest: BehaviorSpec({
    extensions(SpringExtension)

    Database.connect("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false;DATABASE_TO_UPPER=false",
        driver = "org.h2.Driver", user = "sa", password = "")

    transaction {
        SchemaUtils.create(MemberTable, BoardTable)

        given("게시판에 글을 쓸 유저 한 명이 주어진다.") {
            val user = transaction {
                MemberTable.insertAndGetId {
                    it[loginId] = "raonpark"
                    it[name] = "박수민"
                    it[password] = "1234"
                    it[authority] = "USER"
                }
            }

            `when`("게시판에 raonpark 아이디를 가진 유저가 글을 쓴다.") {
                transaction {
                    BoardTable.insert {
                        it[title] = "테일러 314CE 사실 분?"
                        it[content] = "테일러 314CE 2년 안됐는데 사실 분 구합니다."
                        it[image] = "asdfawerw"
                        it[username] = user
                        it[published] = CurrentDateTime
                        it[type] = TypeOfBoard.TRADING
                    }
                }

                then("게시판에 새로운 글 하나가 올라왔다.") {
                    transaction { BoardTable.selectAll().count() } shouldBeGreaterThan 0
                }
            }
        }

        given("게시판에 글을 하나 올린다.") {
            val user = transaction {
                MemberTable.insertAndGetId {
                    it[loginId] = "raonpark"
                    it[name] = "박수민"
                    it[password] = "1234"
                    it[authority] = "USER"
                }
            }
            val board = transaction {
                BoardTable.insertAndGetId {
                    it[title] = "테일러 314CE 사실 분?"
                    it[content] = "테일러 314CE 2년 안됐는데 사실 분 구합니다."
                    it[image] = "asdfawerw"
                    it[username] = user
                    it[published] = CurrentDateTime
                    it[type] = TypeOfBoard.TRADING
                }
            }
            `when`("게시판에 글을 업데이트한다.") {
                transaction {
                    BoardTable.update({ BoardTable.id eq board }) {
                        it[content] = "테일러 314CE 2년 안됐는데 사실 분 구합니다. 250만원 생각하고 있어요! 위치는 도림동입니다."
                    }
                }
                then("업데이트 된 내용을 확인한다.") {
                    val content = transaction {
                        BoardTable.selectAll().where { BoardTable.id eq board }.singleOrNull()?.let {
                            it[BoardTable.content]
                        }
                    }
                    content shouldBe "테일러 314CE 2년 안됐는데 사실 분 구합니다. 250만원 생각하고 있어요! 위치는 도림동입니다."
                }
            }
        }

        given("게시물이 주어진다.") {
            val user = transaction {
                MemberTable.insertAndGetId {
                    it[loginId] = "raonpark"
                    it[name] = "박수민"
                    it[password] = "1234"
                    it[authority] = "USER"
                }
            }
            val board = transaction {
                BoardTable.insertAndGetId {
                    it[title] = "테일러 314CE 사실 분?"
                    it[content] = "테일러 314CE 2년 안됐는데 사실 분 구합니다."
                    it[image] = "asdfawerw"
                    it[username] = user
                    it[published] = CurrentDateTime
                    it[type] = TypeOfBoard.TRADING
                }
            }
            `when`("TypeOfBoard.FREE 인 자유게시판 게시글을 삭제한다.") {
                val ret = transaction {
                    BoardTable.deleteWhere { type eq TypeOfBoard.FREE }
                }
                then("삭제한 값이 0이 나와야한다.") {
                    ret shouldBe 0
                }
            }
        }
    }
})