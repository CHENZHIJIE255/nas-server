package com.max.nas.files

import com.max.nas.common.Messages
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(ApiException::class)
    fun handleApi(e: ApiException): ResponseEntity<Map<String, String?>> =
        ResponseEntity(mapOf(Messages.CODE to e.code, Messages.ERROR to e.message), e.status)

    // FileTool.jailed 抛的越界 → 统一 400
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadArg(e: IllegalArgumentException): ResponseEntity<Map<String, String?>> =
        ResponseEntity(mapOf(Messages.CODE to "BAD_REQUEST", Messages.ERROR to e.message), HttpStatus.BAD_REQUEST)

    // 兜底：堆栈只进服务器日志，不出门
    @ExceptionHandler(Exception::class)
    fun handleOther(e: Exception): ResponseEntity<Map<String, String>> {
        log.error("未捕获异常", e)
        return ResponseEntity(mapOf(Messages.CODE to "INTERNAL", Messages.ERROR to "服务器开小差了"), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}