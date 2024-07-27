package com.example.jwtauth.config

import com.example.jwtauth.vo.Member
import com.example.jwtauth.entity.MemberEntity
import com.example.jwtauth.vo.MemberId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class CustomUserDetailsService: UserDetailsService {
    override fun loadUserByUsername(loginId: String?): UserDetails {
        if(loginId == null)
            throw Exception()
        val member = transaction {
            MemberEntity.selectAll().where { MemberEntity.loginId eq loginId }.single().let {
                Member(
                    id = MemberId(it[MemberEntity.id].value),
                    loginId = it[MemberEntity.loginId],
                    password = it[MemberEntity.password],
                    name = it[MemberEntity.name],
                    authority = it[MemberEntity.authority]
                )
            }
        }

        return CustomUserDetails(member)
    }
}