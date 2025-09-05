package com.example.test.anno

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FUNCTION

/**
 * API版本控制注解
 *
 * 该注解用于标记控制器类或方法支持的API版本，可以指定单个版本或版本范围。
 * 结合自定义的请求映射处理器，可以实现基于请求头的API版本控制功能。
 * 
 * 使用场景：
 * - 当API需要向后兼容时，可以为不同版本的API定义不同的处理方法
 * - 当需要逐步废弃旧版API时，可以通过版本控制引导客户端迁移到新版本
 * 
 * 使用示例：
 * ```kotlin
 * // 指定单个版本
 * @ApiVersion(value = 2)
 * @GetMapping("/resource")
 * fun getResourceV2(): ResponseEntity<Resource> { ... }
 * 
 * // 指定版本范围
 * @ApiVersion(min = 1, max = 3)
 * @GetMapping("/resource")
 * fun getResourceV1to3(): ResponseEntity<Resource> { ... }
 * 
 * // 指定最小版本（不限制最大版本）
 * @ApiVersion(min = 4)
 * @GetMapping("/resource")
 * fun getResourceV4Plus(): ResponseEntity<Resource> { ... }
 * ```
 */
@Target(CLASS, FUNCTION)
@Retention(RUNTIME)
@MustBeDocumented
annotation class ApiVersion(
    /**
     * 指定单个版本号
     * 默认值为-1，表示未指定单个版本，此时将使用min和max定义的版本范围
     */
    val value: Int = -1,
    
    /**
     * 版本范围的下限（包含）
     * 当未指定value时，该参数用于定义支持的最小版本号
     * 默认值为1
     */
    val min: Int = 1,
    
    /**
     * 版本范围的上限（包含）
     * 当未指定value时，该参数用于定义支持的最大版本号
     * 默认值为Int.MAX_VALUE，表示不限制最大版本
     */
    val max: Int = Int.MAX_VALUE,
    
    /**
     * 用于获取版本信息的请求头名称
     * 客户端需要在请求头中通过该名称传递版本号
     * 默认值为"version"
     */
    val header: String = "version"
)