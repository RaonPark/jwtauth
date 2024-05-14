package com.example.jwtauth.controller

import com.example.jwtauth.dto.MemberResponse
import com.example.jwtauth.entity.MemberEntity
import com.example.jwtauth.entity.MemberId
import com.example.jwtauth.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class MemberController(
    private val memberService: MemberService
) {
    @GetMapping("/member/{id}")
    fun getMemberById(@PathVariable id: UUID): ResponseEntity<MemberResponse> {
        val member = memberService.findMemberById(MemberId(id))

        return if(member != null) {
            ResponseEntity.ok(
                MemberResponse(
                    id = member.id.value,
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
        val member = memberService.login
    }
}