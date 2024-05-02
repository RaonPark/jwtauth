package com.example.jwtauth.dao

import com.example.jwtauth.entity.Member
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class MemberDao(memberId: EntityID<UUID>): UUIDEntity(memberId) {
    companion object: UUIDEntityClass<MemberDao>(Member)

    var loginId by Member.loginId
    var username by Member.name
    var password by Member.password
    var authority by Member.authority
}