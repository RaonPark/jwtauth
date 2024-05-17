package com.example.jwtauth.config

import com.example.jwtauth.entity.Member
import com.example.jwtauth.entity.MemberEntity
import com.example.jwtauth.entity.MemberId
import com.example.jwtauth.service.MemberService
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.util.*

@Component
class CustomUserDetailsService: UserDetailsService {
    override fun loadUserByUsername(loginId: String?): UserDetails {
        if(loginId == null)
            throw Exception()
        val member = transaction {
            MemberEntity.select(MemberEntity.loginId eq loginId).limit(1).single().let {
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