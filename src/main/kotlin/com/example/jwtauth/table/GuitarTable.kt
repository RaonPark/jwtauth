package com.example.jwtauth.table

import com.example.jwtauth.vo.Guitar
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.springframework.stereotype.Component

@Component
object GuitarTable: IntIdTable() {
    val company = reference("guitarCompany", GuitarCompanyTable)
    val model = varchar("model", 50)
    val price = integer("price")
    val isUsed = bool("isUsed")
}

fun GuitarTable.rowToGuitarVO(row: ResultRow): Guitar =
    Guitar(
        id = row[id].value, model = row[model], price = row[price], isUsed = row[isUsed]
    )