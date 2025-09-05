package com.example.test.config

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

/**
 * API版本控制的Web配置类
 *
 * 该配置类通过实现WebMvcRegistrations接口，替换Spring MVC默认的RequestMappingHandlerMapping，
 * 使其支持基于@ApiVersion注解的API版本控制功能。
 * 
 * 在Spring Boot应用中，WebMvcRegistrations接口提供了一种机制来自定义Spring MVC的核心组件。
 * 通过重写getRequestMappingHandlerMapping方法，我们可以提供自定义的请求映射处理器，
 * 从而实现诸如API版本控制、自定义路由规则等高级功能。
 * 
 * 该配置的作用是让Spring MVC在处理请求时，能够识别并应用@ApiVersion注解中定义的版本规则，
 * 根据请求头中的版本信息将请求路由到正确的处理方法。
 */
@Configuration
class VersioningWebConfig : WebMvcRegistrations {
    /**
     * 提供自定义的RequestMappingHandlerMapping实例，用于支持API版本控制
     * 
     * 通过将VersionRequestMappingHandlerMapping的order设置为0，确保它优先于Spring默认的处理器执行，
     * 从而正确应用版本控制规则。
     *
     * @return 支持API版本控制的VersionRequestMappingHandlerMapping实例
     */
    override fun getRequestMappingHandlerMapping(): RequestMappingHandlerMapping {
        return VersionRequestMappingHandlerMapping().apply { order = 0 }
    }
}