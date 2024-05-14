package com.example.jwtauth.service

import com.example.jwtauth.entity.Member
import com.example.jwtauth.entity.MemberEntity
import com.example.jwtauth.entity.MemberId
import org.jetbrains.exposed.sql.selectAll
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
@Transactional
class MemberService(
    private val passwordEncoder: PasswordEncoder
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
        val member = MemberEntity.selectAll().where { MemberEntity.loginId eq loginId }.firstOrNull()?.let {

        }
        passwordEncoder.encode(password).matches()
    }
}