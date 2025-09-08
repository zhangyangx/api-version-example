package com.example.test.config

import com.example.test.anno.ApiVersion
import com.example.test.anno.header
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import java.lang.reflect.Method

/**
 * 支持API版本控制的请求映射处理器映射
 *
 * 扩展Spring MVC的RequestMappingHandlerMapping，通过重写getCustomTypeCondition和getCustomMethodCondition方法，
 * 为带有@ApiVersion注解的控制器和方法创建自定义的请求条件，从而实现API版本控制功能。
 * 当请求到达时，Spring MVC会根据请求头中的版本信息与控制器/方法上定义的版本条件进行匹配，
 * 选择最适合的处理方法来响应请求。
 */
class VersionRequestMappingHandlerMapping : RequestMappingHandlerMapping() {
    /**
     * 获取控制器类上的版本条件
     *
     * @param handlerType 控制器类类型
     * @return 如果类上有@ApiVersion注解，则返回对应的ApiVersionRequestCondition对象，否则返回null
     */
    override fun getCustomTypeCondition(handlerType: Class<*>): ApiVersionRequestCondition? {
        val av = handlerType.getAnnotation(ApiVersion::class.java)
        return av?.let {
            ApiVersionRequestCondition(it.value, it.min, it.max, header)
        }
    }

    /**
     * 获取控制器方法上的版本条件
     *
     * @param method 控制器方法
     * @return 如果方法上有@ApiVersion注解，则返回对应的ApiVersionRequestCondition对象，否则返回null
     */
    override fun getCustomMethodCondition(method: Method): ApiVersionRequestCondition? {
        val av = method.getAnnotation(ApiVersion::class.java)
        return av?.let {
            ApiVersionRequestCondition(it.value, it.min, it.max, header)
        }
    }
}