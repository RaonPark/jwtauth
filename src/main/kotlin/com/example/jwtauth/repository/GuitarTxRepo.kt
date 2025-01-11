package com.example.jwtauth.repository

import com.example.jwtauth.table.GuitarTxTable
import com.example.jwtauth.table.rowToVO
import com.example.jwtauth.vo.GuitarTx
import org.jetbrains.exposed.sql.insertAndGetId
import org.springframework.stereotype.Repository

@Repository
class GuitarTxRepo {
    fun insert(guitarTx: GuitarTx): Int =
        GuitarTxTable.insertAndGetId {
            it[guitarName] = guitarTx.guitarName
            it[price] = guitarTx.price
            it[county] = guitarTx.county
            it[txTime] = guitarTx.txTime
        }.value


    fun findById(guitarTxId: Int): GuitarTx {
        return GuitarTxTable.select(
            GuitarTxTable.guitarName, GuitarTxTable.price, GuitarTxTable.county, GuitarTxTable.txTime)
            .where { GuitarTxTable.id eq guitarTxId }
            .single()
            .let {
                GuitarTxTable.rowToVO(it)
            }
    }
}