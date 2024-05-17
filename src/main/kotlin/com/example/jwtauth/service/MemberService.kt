package com.example.jwtauth.service

import com.example.jwtauth.dto.MemberRequest
import com.example.jwtauth.dto.MemberResponse
import com.example.jwtauth.entity.Member
import com.example.jwtauth.entity.MemberEntity
import com.example.jwtauth.entity.MemberId
import com.example.jwtauth.jwt.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
@Transactional
class MemberService(
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun findMemberById(id: MemberId): Member? {
        return MemberEntity.selectAll().where { MemberEntity.id eq id.value }.firstOrNull()?.let {
            Member(
                id = MemberId(it[MemberEntity.id].value),
                name = it[MemberEntity.name],
                loginId = it[MemberEntity.loginId],
                password = it[MemberEntity.password],
                authority = it[MemberEntity.authority],
            )
        }
    }

    fun login(loginId: String, password: String): Boolean {
        val memberPw = MemberEntity.select(MemberEntity.loginId eq loginId).limit(1).map { it[MemberEntity.password] }
            .singleOrNull().let { "unknownPassword" }
        return passwordEncoder.matches(password, memberPw)
    }

    fun getMemberByToken(request: HttpServletRequest): MemberResponse? {
        val response = request.cookies.map {
            if(it.name == "JWT") {
                val userDetails = jwtTokenProvider.getUserDetails(it.value)
                return@map MemberEntity.select(MemberEntity.name eq userDetails.username).limit(1).singleOrNull()?.let { resultRow ->
                    MemberResponse(
                        id = MemberId(resultRow[MemberEntity.id].value).value,
                        loginId = resultRow[MemberEntity.loginId],
                        name = resultRow[MemberEntity.name],
                        authority = resultRow[MemberEntity.authority]
                    )
                }!!
            } else {
                return@map MemberResponse(
                    id = MemberId(UUID.randomUUID()).value,
                    loginId = "ANONYMOUS",
                    name = "ANONYMOUS",
                    authority = "NOOP"
                )
            }
        }

        return response[0]
    }

    fun create(member: MemberRequest): MemberId {
        val id = MemberEntity.insertAndGetId {
            it[loginId] = member.loginId
            it[password] = passwordEncoder.encode(member.password)
            it[name] = member.name
            it[authority] = member.role
        }

        return MemberId(id.value)
    }
}