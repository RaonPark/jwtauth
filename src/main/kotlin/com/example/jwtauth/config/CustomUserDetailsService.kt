package com.example.jwtauth.config

import com.example.jwtauth.dao.MemberDao
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CustomUserDetailsService: UserDetailsService {
    override fun loadUserByUsername(memberId: String?): UserDetails {
        val member = MemberDao.findById(UUID.fromString(memberId)) ?: throw Exception()
        return CustomUserDetails(member)
    }
}