package com.example.test.anno

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FUNCTION

/**
 * API版本控制注解
 *
 * 该注解用于标记控制器类或方法支持的API版本，可以指定单个版本或版本范围。
 * 结合自定义的请求映射处理器，可以实现基于请求头的API版本控制功能。
 * 支持语义化版本号格式，如0.0.1、1.0.1等。
 *
 * 使用场景：
 * - 当API需要向后兼容时，可以为不同版本的API定义不同的处理方法
 * - 当需要逐步废弃旧版API时，可以通过版本控制引导客户端迁移到新版本
 *
 * 使用示例：
 * ```kotlin
 * // 指定单个版本
 * @ApiVersion(value = "1.0.0")
 * @GetMapping("/resource")
 * fun getResourceV1(): ResponseEntity<Resource> { ... }
 *
 * // 指定版本范围
 * @ApiVersion(min = "1.0.0", max = "1.2.0")
 * @GetMapping("/resource")
 * fun getResourceV1to12(): ResponseEntity<Resource> { ... }
 *
 * // 指定最小版本（不限制最大版本）
 * @ApiVersion(min = "2.0.0")
 * @GetMapping("/resource")
 * fun getResourceV2Plus(): ResponseEntity<Resource> { ... }
 * ```
 * [api 向前兼容](https://dy3m1s1v7v.feishu.cn/docx/UgGldF3Ymo0N4Qxo58Ac9TZ3nId)
 * @author yang.zhang
 * @since 2025/9/8
 */
@Target(CLASS, FUNCTION)
@Retention(RUNTIME)
@MustBeDocumented
annotation class ApiVersion(
    /**
     * 指定单个语义化版本号（如1.0.0、2.1.3等）
     * 默认值为空字符串，表示未指定单个版本，此时将使用min和max定义的版本范围
     */
    val value: String = "",

    /**
     * 版本范围的下限（包含）
     * 当未指定value时，该参数用于定义支持的最小版本号
     * 默认值为"0.0.1"，表示从第一个版本开始
     */
    val min: String = "0.0.1",

    /**
     * 版本范围的上限（包含）
     * 当未指定value时，该参数用于定义支持的最大版本号
     * 默认值为空字符串，表示不限制最大版本
     */
    val max: String = "",

    /**
     * 用于获取版本信息的请求头名称
     * 客户端需要在请求头中通过该名称传递版本号
     * 默认值为"api-version"
     */
    val header: String = "api-version"
)

/**
 * 默认的请求头名称，保持向后兼容
 */
val header: String
    get() = "api-version"