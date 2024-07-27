package com.example.jwtauth.service

import com.example.jwtauth.dto.CreateGuitarCompanyRequest
import com.example.jwtauth.dto.CreateGuitarRequest
import com.example.jwtauth.entity.GuitarCompanyEntity
import com.example.jwtauth.entity.GuitarEntity
import com.example.jwtauth.table.GuitarCompanyTable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class GuitarService {
    val guitarRepository = GuitarEntity
    val guitarCompanyRepository = GuitarCompanyEntity
    fun receiveNewGuitar(guitarDto: CreateGuitarRequest): Int {
        val guitarCompany = guitarCompanyRepository.find { GuitarCompanyTable.companyName eq guitarDto.companyName }
                .single()

        val id = guitarRepository.new {
            model = guitarDto.model
            isUsed = guitarDto.isUsed
            price = guitarDto.price
            company = guitarCompany
        }.id

        return id.value
    }

    fun createNewGuitarCompany(guitarCompany: CreateGuitarCompanyRequest): Int {
        return guitarCompanyRepository.new {
            companyName = guitarCompany.companyName
            established = guitarCompany.established
        }.id.value
    }
}