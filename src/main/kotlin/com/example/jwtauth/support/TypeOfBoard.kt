package com.example.jwtauth.support

enum class TypeOfBoard(val type: String) {
    FREE("자유 게시판"),
    TRADING("거래 게시판"),
    FAQ("FAQ 게시판"),
    MANAGE("관리 게시판"),
    ;
    companion object {
        fun fromType(type: String): TypeOfBoard? {
            return entries.firstOrNull { it.type == type }
        }
    }
}