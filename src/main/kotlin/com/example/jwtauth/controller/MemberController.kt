package com.example.jwtauth.controller

import com.example.jwtauth.dto.MemberRequest
import com.example.jwtauth.dto.MemberResponse
import com.example.jwtauth.vo.MemberId
import com.example.jwtauth.service.MemberService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class MemberController(
    private val memberService: MemberService,
) {
    @GetMapping("/member/{id}")
    fun getMemberById(@PathVariable id: UUID): ResponseEntity<MemberResponse> {
        val member = memberService.findMemberById(MemberId(id))

        return if(member != null) {
            ResponseEntity.ok(
                MemberResponse(
                    loginId = member.loginId,
                    name = member.name,
                    authority = member.authority
                )
            )
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/member/login")
    fun login(@RequestParam("loginId") loginId: String, @RequestParam("password") password: String): ResponseEntity<String> {
        val isSuccess = memberService.login(loginId, password)
        return if(isSuccess) {
            ResponseEntity.ok("loginSuccess")
        } else {
            ResponseEntity.badRequest().body("loginFailed")
        }
    }

    @GetMapping("/member/jwt")
    fun getMemberByToken(request: HttpServletRequest): ResponseEntity<MemberResponse> {
        val memberResponse = memberService.getMemberByToken(request)

        return if(memberResponse != null) {
            ResponseEntity.ok(memberResponse)
        } else {
            ResponseEntity.badRequest().body(null)
        }
    }

    @PostMapping("/member/register")
    fun register(@RequestBody member: MemberRequest): ResponseEntity<UUID> {
        val id = memberService.create(member)

        return ResponseEntity.ok(id.value)
    }

    @PostMapping("/member/logout")
    fun logout(authentication: Authentication, request: HttpServletRequest, response: HttpServletResponse): String {
        return "logoutOK"
    }
}