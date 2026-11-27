package com.max.nas.auth

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

/** 会话：有状态 token 的实体 */
@Entity
@Table(name = "sessions")
class Session(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(name = "token_hash", unique = true, nullable = false, length = 64)
    var tokenHash: String = "",              // 只存 SHA-256，不存明文 token

    @Column(name = "user_id", nullable = false)
    var userId: UUID = UUID.randomUUID(),

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "expires_at", nullable = false)
    var expiresAt: Instant = Instant.now(),

    @Column(name = "last_seen_at")
    var lastSeenAt: Instant? = null,

    @Column(length = 64)
    var ip: String? = null,

    @Column(name = "user_agent", length = 512)
    var userAgent: String? = null,

    @Column(nullable = false)
    var revoked: Boolean = false,            // 踢设备 = 置 true
)