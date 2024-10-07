package com.example.jwtauth.service

import com.example.jwtauth.support.TypeOfBoard
import com.example.jwtauth.table.*
import com.example.jwtauth.vo.Board
import com.example.jwtauth.vo.Member
import com.example.jwtauth.vo.MemberId
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.datetime.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import java.util.UUID

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = ["classpath:application-test.yml"])
object BoardServiceTest: BehaviorSpec({

    extensions(SpringExtension)

    lateinit var user: Member
    lateinit var newBoard: Board
    lateinit var userId: EntityID<UUID>

    val memberTable: MemberTable = mockk(relaxed = true)
    val boardTable: BoardTable = mockk(relaxed = true)
    val boardService = BoardService(boardTable)

    Database.connect(url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false;DATABASE_TO_UPPER=false",
        driver = "org.h2.Driver", user = "sa")

    transaction {
        SchemaUtils.create(MemberTable, BoardTable, GuitarTable, GuitarCompanyTable)
    }

    user = Member(loginId = "martin", password = "12345678", authority = "user", name = "martin")
    newBoard = Board(title="테일러 314CE 사실분?", content="테일러 314CE 2년 썼는데 사실 분 구합니다.", username = "martin",
        image="thisImage", published = Clock.System.now().toLocalDateTime(TimeZone.of("Asia/Seoul")),
        type=TypeOfBoard.TRADING)

    mockkStatic("org.jetbrains.exposed.sql.transactions.ThreadLocalTransactionManagerKt")

    given("게시판에 게시물 1개 올리기") {
        `when`("거래 게시판에 martin이라는 이름의 유저가 게시물을 올린다.") {
            val boardId = boardService.publishArticle(newBoard)
            then("boardId에 제대로 된 ID 값이 나온다.") {
                boardId shouldBeGreaterThan 0
            }
        }
    }

    xgiven("게시판에 게시물 삭제하기") {
        `when`("거래 게시판에 있는 martin라는 이름의 유저가 게시물을 삭제한다.") {

        }
    }

    xgiven("게시판에 게시물 업데이트하기") {
        `when`("거래 게시판에 있는 ") {

        }
    }

    afterTest {
        unmockkAll()
    }
})