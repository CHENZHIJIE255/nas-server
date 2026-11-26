package com.max.nas.files

import org.springframework.http.HttpStatus

/** 业务异常：HTTP 状态 + 机器可读业务码 + 人类可读消息 */
class ApiException(
    val status: HttpStatus,
    val code: String,
    message: String
) : RuntimeException(message)