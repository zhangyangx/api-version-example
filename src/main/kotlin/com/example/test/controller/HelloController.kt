package com.example.test.controller

import com.example.test.anno.ApiVersion
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 示例控制器，演示基于请求头的API版本控制
 *
 * 该控制器通过[ApiVersion]注解为相同的路径提供不同版本的实现，
 * 根据请求头中的api-version参数来路由到匹配的处理方法。
 * 现在支持语义化版本号格式，如0.0.1、1.0.1等。
 */
@RestController
@RequestMapping("/api")
class HelloController {

    /**
     * 为API版本1.0.0到2.0.0提供的hello接口
     *
     * @return 欢迎消息，包含版本范围信息
     */
    @ApiVersion(min = "1.0.0", max = "2.0.0")
    @GetMapping("/hello")
    fun helloV1to2(): String = "hello v1.0.0~v2.0.0"

    @ApiVersion(min = "10.0.0")
    @GetMapping("/hello")
    fun helloVMin(): String = "min version: 10.0.0+"

    @ApiVersion(max = "20.0.0")
    @GetMapping("/hello")
    fun helloVMax(): String = "max version: <=20.0.0"

    /**
     * 为API版本3.0.0提供的hello接口
     *
     * @return 欢迎消息，仅适用于版本3.0.0
     */
    @ApiVersion(value = "3.0.0")
    @GetMapping("/hello")
    fun helloV3(): String = "hello v3.0.0 only"

    /**
     * 为API版本4.0.0及以上提供的hello接口
     *
     * @return 欢迎消息，适用于版本4.0.0及更高版本
     */
    @ApiVersion(min = "4.0.0")
    @GetMapping("/hello")
    fun helloV4Plus(): String = "hello v4.0.0+"

    /**
     * 默认的hello接口，当请求没有版本头或版本不匹配时使用
     *
     * @param version 请求头中的版本信息（可选）
     * @return 欢迎消息，包含实际接收到的版本头信息
     */
    @GetMapping("/hello")
    fun helloDefault(@RequestHeader(value = "api-version", required = false) version: String?): String {
        return "hello default (header=api-version:$version)"
    }
}