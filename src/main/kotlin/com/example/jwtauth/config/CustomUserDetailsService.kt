package com.example.jwtauth.config

import com.example.jwtauth.entity.MemberId
import com.example.jwtauth.service.MemberService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.util.*

@Component
class CustomUserDetailsService(
    private val memberService: MemberService
): UserDetailsService {
    override fun loadUserByUsername(memberId: String?): UserDetails {
        val member = memberService.findMemberById(MemberId(UUID.fromString(memberId))) ?: throw Exception()
        return CustomUserDetails(member)
    }
}