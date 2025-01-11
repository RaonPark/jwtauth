package com.example.jwtauth.table

import com.example.jwtauth.support.KindOfTime
import com.example.jwtauth.vo.GuitarTx
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.springframework.stereotype.Component

object GuitarTxTable: IntIdTable("guitarTxId") {
    val guitarName = varchar("guitarName", length = 100)
    val price = long("price")
    val txTime = enumerationByName<KindOfTime>(name = "txTime", length = 30)
    val county = varchar("county", length = 10)
}

fun GuitarTxTable.rowToVO(row: ResultRow): GuitarTx =
    GuitarTx(
        price = row[price], txTime = row[txTime], guitarName = row[guitarName], county = row[county]
    )
