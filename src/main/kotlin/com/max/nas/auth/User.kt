package com.max.nas.auth

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "users")
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,                    // 新建时没 id，save 后才生成

    @Column(unique = true, nullable = false, length = 32)
    var account: String = "",                // 数字账号，唯一

    @Column(nullable = false, length = 64)
    var username: String = "",               // 昵称，可重复

    @Column(name = "password_hash", nullable = false, length = 100)
    var passwordHash: String = "",           // bcrypt 哈希，绝不存明文

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role = Role.USER,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: UserStatus = UserStatus.ACTIVE,

    @Column(name = "quota_bytes", nullable = false)
    var quotaBytes: Long = 0,                // 0 = 不限制

    @Column(name = "used_bytes", nullable = false)
    var usedBytes: Long = 0,                 // 含在途的已占用量

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "deleted_at")
    var deletedAt: Instant? = null,          // 非空 = 软删除
)