package com.max.nas.auth

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

/** 登录审计：成功失败都记，只增不删 */
@Entity
@Table(name = "login_audit")
class LoginAudit(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false, length = 32)
    var account: String = "",                // 尝试登录的账号（失败也可能记）

    @Column(name = "user_id")
    var userId: UUID? = null,                // 成功时有值；失败/账号不存在为 null

    @Column(nullable = false)
    var success: Boolean = false,

    @Column(length = 64)
    var ip: String? = null,

    @Column(name = "user_agent", length = 512)
    var userAgent: String? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),
)