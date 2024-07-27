package com.example.jwtauth.entity

import com.example.jwtauth.table.GuitarTable
import com.example.jwtauth.table.GuitarCompanyTable
import com.example.jwtauth.vo.Guitar
import com.example.jwtauth.vo.GuitarCompany
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

class GuitarCompanyEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<GuitarCompanyEntity>(GuitarCompanyTable)
    val guitars by GuitarEntity referrersOn GuitarTable.company
    var companyName by GuitarCompanyTable.companyName
    var established by GuitarCompanyTable.established
}

fun GuitarCompanyEntity.toVO(): GuitarCompany = GuitarCompany(id.value, companyName, established)