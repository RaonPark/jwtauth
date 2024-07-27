package com.example.jwtauth.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object GuitarCompanyTable: IntIdTable() {
    val companyName = varchar("companyName", 50).uniqueIndex()
    val established = datetime("established").defaultExpression(CurrentDateTime)
}