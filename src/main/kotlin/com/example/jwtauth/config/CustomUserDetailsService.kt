package com.example.jwtauth.config

import com.example.jwtauth.vo.Member
import com.example.jwtauth.table.MemberTable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class CustomUserDetailsService: UserDetailsService {
    override fun loadUserByUsername(loginId: String?): CustomUserDetails {
        if(loginId == null)
            throw Exception()
        val member = transaction {
            MemberTable.selectAll().where { MemberTable.loginId eq loginId }.single().let {
                Member(
                    loginId = it[MemberTable.loginId],
                    password = it[MemberTable.password],
                    name = it[MemberTable.name],
                    authority = it[MemberTable.authority]
                )
            }
        }

        return CustomUserDetails(member)
    }
}