package com.max.nas.common

/**
 * 全局消息常量：JSON 契约 key + 人类可读消息 + 默认值。
 * 契约 key 永不改、永不翻译；消息暂为中文，将来 i18n 时把"值"挪进 messages.properties。
 */
object Messages {

    // ── JSON 契约 key（与前端签的协议）──
    const val OK = "ok"
    const val ERROR = "error"
    const val NAME = "name"
    const val SIZE = "size"
    const val TYPE = "type"
    const val PATH = "path"
    const val ENTRIES = "entries"
    const val DELETED = "deleted"
    const val CREATED = "created"
    const val CPU_CORES = "cpuCores"
    const val JAVA_VERSION = "javaVersion"
    const val MAX_MEMORY_MB = "maxMemoryMB"
    const val MESSAGE = "message"

    // ── 契约值 ──
    const val TYPE_DIR = "dir"
    const val TYPE_FILE = "file"

    // ── 默认值 ──
    const val ROOT_PATH = "/"
    const val UNNAMED_FILE = "unnamed"

    // ── 人类可读消息（%s 占位符；i18n 时值挪进 properties，名字照搬）──
    const val PATH_OUT_OF_BOUNDS = "路径越界: %s"
    const val DIR_NOT_FOUND = "目录不存在: %s"
    const val UPLOAD_DIR_NOT_FOUND = "目标目录不存在: %s"
    const val ROOT_DELETE_FORBIDDEN = "禁止删除数据根目录"
    const val NOT_FOUND = "不存在: %s"
    const val DELETE_FAILED = "删除失败"
    const val ALREADY_EXISTS = "已存在: %s"
    const val CREATE_FAILED = "创建失败"
    const val SERVER_RUNNING = "NAS 服务器运行中"
}