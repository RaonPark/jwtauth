package com.example.jwtauth.controller

import com.example.jwtauth.dto.CreateGuitarCompanyRequest
import com.example.jwtauth.dto.CreateGuitarRequest
import com.example.jwtauth.service.GuitarService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/guitar")
class GuitarController(
    val guitarService: GuitarService
) {
    @PostMapping("/newGuitar")
    fun buyNewGuitar(@RequestBody guitarDto: CreateGuitarRequest): Int {
        return guitarService.receiveNewGuitar(guitarDto)
    }

    @PostMapping("/newGuitarCompany")
    fun launchNewGuitarCompany(@RequestBody guitarCompanyRequest: CreateGuitarCompanyRequest): Int {
        return guitarService.createNewGuitarCompany(guitarCompanyRequest)
    }
}