package com.max.nas.auth

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID> {
    fun findByAccount(account: String): User?
    fun countByDeletedAtIsNull(): Long       // 活着的用户数 = 0 时注册门才开
}

interface SessionRepository : JpaRepository<Session, UUID> {
    fun findByTokenHash(tokenHash: String): Session?
    fun findAllByUserId(userId: UUID): List<Session>
    fun deleteAllByUserId(userId: UUID)      // 删用户/踢全部设备时级联
}

interface LoginAuditRepository : JpaRepository<LoginAudit, UUID>   // save/findAll 白送

interface IpBanRepository : JpaRepository<IpBan, UUID> {
    fun findByIp(ip: String): IpBan?
}