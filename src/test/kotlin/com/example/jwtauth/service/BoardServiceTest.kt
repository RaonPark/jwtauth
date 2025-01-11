package com.example.jwtauth.service

import com.example.jwtauth.support.TypeOfBoard
import com.example.jwtauth.table.BoardTable
import com.example.jwtauth.table.MemberTable
import com.example.jwtauth.vo.Board
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldNotBe
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest(classes = [BoardServiceTest::class])
@ActiveProfiles("test")
@TestPropertySource(locations = ["classpath:application-test.yml"])
object BoardServiceTest: BehaviorSpec({
    extensions(SpringExtension)

    // relaxed = true 옵션은 primitive(int, double, String 등등)은 모두 기본값(default)을 반환한다.

    Database.connect("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false;DATABASE_TO_UPPER=false",
        driver = "org.h2.Driver", user = "sa", password = "")

    transaction {
        SchemaUtils.create(BoardTable, MemberTable)

        val boardService = BoardService(BoardTable, MemberTable)
        given("raonpark이라는 이름의 유저가 주어진다.") {
            val user = transaction {
                MemberTable.insertAndGetId {
                    it[loginId] = "raonpark"
                    it[name] = "박수민"
                    it[password] = "1234"
                    it[authority] = "USER"
                }
            }

            val board = Board(
                title = "안녕하세요!",
                content = "기타 3년친 뉴비입니다. 잘 부탁드립니다.",
                image = "",
                username = "박수민",
                published = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                type = TypeOfBoard.FREE
            )

            `when`("유저가 게시판에 게시글을 집어넣는다.") {

                val id = boardService.publishArticle(board)

                then("반드시 id를 반환해야한다.") {
                    id shouldBeGreaterThan 0
                    println(id)
                }
            }
        }

        given("게시글과 유저 하나가 주어진다.") {
            val user = transaction {
                MemberTable.insertAndGetId {
                    it[loginId] = "raonpark"
                    it[name] = "박수민"
                    it[password] = "1234"
                    it[authority] = "USER"
                }
            }

            val board = Board(
                title = "안녕하세요!",
                content = "기타 3년친 뉴비입니다. 잘 부탁드립니다.",
                image = "",
                username = "박수민",
                published = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                type = TypeOfBoard.FREE
            )
            `when`("게시판에 올리고 삭제한다.") {

                val publishResult = boardService.publishArticle(board)
                val result = boardService.deleteArticle(publishResult)

                then("삭제한 로우가 1개 이상이어야 한다.") {
                    result shouldBeGreaterThan 0
                }
            }
        }

        given("게시물의 내용을 변경하기 위한 게시물과 유저가 주어진다.") {
            val user = transaction {
                MemberTable.insertAndGetId {
                    it[loginId] = "raonpark"
                    it[name] = "박수민"
                    it[password] = "1234"
                    it[authority] = "USER"
                }
            }

            val board = Board(
                title = "안녕하세요!",
                content = "기타 3년친 뉴비입니다. 잘 부탁드립니다.",
                image = "",
                username = "박수민",
                published = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                type = TypeOfBoard.FREE
            )

            `when`("게시물을 업로드하고 게시물의 내용을 변경한다.") {
                boardService.publishArticle(board)

                board.content = "기타 3년친 뉴비입니다. 잘 부탁드립니다. 서울에 살고 있어요."

                then("게시물의 내용이 변경된 내용과 같아야한다.") {

                }
            }
        }
    }
})