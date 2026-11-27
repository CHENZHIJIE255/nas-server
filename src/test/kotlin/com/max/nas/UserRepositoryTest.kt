package com.max.nas.auth

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserRepositoryTest @Autowired constructor(
    private val users: UserRepository,
    private val sessions: SessionRepository,
) {

    //用户和会话的基本读写
    @Test
    fun savesAndFindsUserAndSession() {
        val u = users.save(User(account = "10001", username = "测试", passwordHash = "hash"))
        assertNotNull(u.id)
        assertEquals(1, users.countByDeletedAtIsNull())

        val s = sessions.save(Session(tokenHash = "abc123", userId = u.id!!))
        assertEquals(u.id, sessions.findByTokenHash("abc123")?.userId)
    }
}