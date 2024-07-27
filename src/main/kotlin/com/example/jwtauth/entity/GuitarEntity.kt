package com.example.jwtauth.entity

import com.example.jwtauth.table.GuitarTable
import com.example.jwtauth.table.rowToGuitarVO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow

class GuitarEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<GuitarEntity>(GuitarTable)
    var company by GuitarCompanyEntity referencedOn GuitarTable.company
    var model by GuitarTable.model
    var price by GuitarTable.price
    var isUsed by GuitarTable.isUsed
}

fun ResultRow.toGuitarVO() = GuitarTable.rowToGuitarVO(this)