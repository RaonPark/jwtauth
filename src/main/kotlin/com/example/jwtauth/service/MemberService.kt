package com.example.jwtauth.service

import com.example.jwtauth.dto.MemberRequest
import com.example.jwtauth.dto.MemberResponse
import com.example.jwtauth.vo.Member
import com.example.jwtauth.table.MemberTable
import com.example.jwtauth.vo.MemberId
import com.example.jwtauth.jwt.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class MemberService(
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun findMemberById(id: MemberId): Member? {
        return MemberTable.selectAll().where { MemberTable.id eq id.value }.firstOrNull()?.let {
            Member(
                name = it[MemberTable.name],
                loginId = it[MemberTable.loginId],
                password = it[MemberTable.password],
                authority = it[MemberTable.authority],
            )
        }
    }

    fun login(loginId: String, password: String): Boolean {
        val memberPw = MemberTable.selectAll().where { MemberTable.loginId eq loginId }
            .singleOrNull()?.let { it[MemberTable.password] }
        return passwordEncoder.matches(password, memberPw)
    }

    fun getMemberByToken(request: HttpServletRequest): MemberResponse? {
        val response = request.cookies.map {
            if(it.name == "JWT") {
                val userDetails = jwtTokenProvider.getUserDetails(it.value)
                return@map MemberTable.selectAll().where { MemberTable.name eq userDetails.username }.singleOrNull()?.let { resultRow ->
                    MemberResponse(
                        loginId = resultRow[MemberTable.loginId],
                        name = resultRow[MemberTable.name],
                        authority = resultRow[MemberTable.authority]
                    )
                }!!
            } else {
                return@map MemberResponse(
                    loginId = "ANONYMOUS",
                    name = "ANONYMOUS",
                    authority = "NOOP"
                )
            }
        }

        return response[0]
    }

    fun create(member: MemberRequest): MemberId {
        val id = MemberTable.insertAndGetId {
            it[loginId] = member.loginId
            it[password] = passwordEncoder.encode(member.password)
            it[name] = member.name
            it[authority] = member.role
        }

        return MemberId(id.value)
    }
}