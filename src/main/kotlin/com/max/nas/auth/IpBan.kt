package com.max.nas.auth

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

/** IP 黑名单（拦截器最先查的，比会话还早）*/
@Entity
@Table(name = "ip_bans")
class IpBan(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(unique = true, nullable = false, length = 64)
    var ip: String = "",

    @Column(length = 255)
    var reason: String? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "expires_at")
    var expiresAt: Instant? = null,          // null = 永久封
)