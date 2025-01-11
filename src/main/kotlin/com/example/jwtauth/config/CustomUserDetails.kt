package com.example.jwtauth.config

import com.example.jwtauth.vo.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(private val member: Member): UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf<SimpleGrantedAuthority>(SimpleGrantedAuthority(member.authority))
    }

    override fun getPassword(): String {
        return member.password
    }

    override fun getUsername(): String {
        return member.name
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    fun getLoginId(): String {
        return member.loginId
    }
}