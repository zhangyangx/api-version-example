package com.example.test.config

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.servlet.mvc.condition.RequestCondition

/**
 * API版本请求条件类
 *
 * 实现Spring MVC的RequestCondition接口，用于匹配请求的API版本是否符合控制器或方法上定义的版本条件。
 * 该类支持两种版本匹配模式：
 * 1. 精确匹配：通过value参数指定一个精确的版本号
 * 2. 范围匹配：通过min和max参数定义一个版本范围
 * 
 * 当一个请求同时匹配多个处理方法时，该类的compareTo方法会根据预定义的优先级规则选择最合适的处理方法。
 */
class ApiVersionRequestCondition(
    /**
     * 精确版本号，-1表示未指定精确版本
     */
    private val value: Int,
    
    /**
     * 版本范围的下限（包含）
     */
    private val min: Int,
    
    /**
     * 版本范围的上限（包含）
     */
    private val max: Int,
    
    /**
     * 版本请求头名称
     */
    private val headerName: String
) : RequestCondition<ApiVersionRequestCondition> {

    /**
     * 合并两个条件，通常是类级别和方法级别的条件合并
     * 
     * 在Spring MVC中，当控制器类和方法上都有@ApiVersion注解时，
     * 方法级别的注解会覆盖类级别的注解，这符合常见的"就近原则"。
     *
     * @param other 要合并的另一个条件（通常是方法级别的条件）
     * @return 合并后的条件，返回other参数表示方法级别的注解覆盖类级别的注解
     */
    override fun combine(other: ApiVersionRequestCondition): ApiVersionRequestCondition {
        // 方法上的注解覆盖类上的注解
        return ApiVersionRequestCondition(other.value, other.min, other.max, other.headerName)
    }

    /**
     * 根据请求判断是否匹配当前条件
     * 
     * 该方法从请求头中提取版本信息，并根据当前条件的配置进行匹配：
     * 1. 如果请求头中没有版本信息或版本号不合法，则返回null，表示不匹配
     * 2. 如果当前条件指定了精确版本号，则只有当请求版本号与之相等时才匹配
     * 3. 如果当前条件定义了版本范围，则当请求版本号在该范围内时匹配
     *
     * @param request HTTP请求对象
     * @return 如果请求的版本符合当前条件，则返回当前条件对象，否则返回null
     */
    override fun getMatchingCondition(request: HttpServletRequest): ApiVersionRequestCondition? {
        val versionHeader = request.getHeader(headerName)
        val reqVer = try {
            versionHeader?.trim()?.toInt() ?: -1
        } catch (e: NumberFormatException) {
            -1
        }

        // 版本信息不合法或未提供，返回null表示不匹配
        if (reqVer == -1) return null

        // 指定版本号优先匹配
        if (value != -1) {
            return if (reqVer == value) this else null
        }

        // 区间匹配
        return if (reqVer in min..max) this else null
    }

    /**
     * 比较两个条件的优先级，决定哪个条件对应的处理方法应该被优先选择
     * 
     * 优先级规则如下（从高到低）：
     * 1. 精确版本匹配优先于范围版本匹配
     * 2. 当两者都是精确版本匹配时，版本号较大的优先
     * 3. 当两者都是范围版本匹配时，最小版本号较大的优先
     * 4. 如果最小版本号相同，则范围较小的优先
     *
     * @param other 要比较的另一个条件
     * @param request HTTP请求对象
     * @return 优先级比较结果，负数表示当前条件优先级更高，正数表示other条件优先级更高，0表示优先级相同
     */
    override fun compareTo(other: ApiVersionRequestCondition, request: HttpServletRequest): Int {
        // 指定版本比区间更具体 → 优先
        if (this.value != -1 && other.value == -1) return -1
        if (this.value == -1 && other.value != -1) return 1

        // 都是指定版本时，版本号大的优先
        if (this.value != -1 && other.value != -1) {
            return other.value.compareTo(this.value)
        }

        // 都是区间时：min 大的优先；若相同，区间更小的优先
        return if (this.min != other.min) {
            other.min.compareTo(this.min)
        } else {
            (this.max - this.min).compareTo(other.max - other.min)
        }
    }

    /**
     * 返回条件的字符串表示，用于日志记录和调试
     *
     * @return 格式化的版本条件字符串
     */
    override fun toString(): String {
        return if (value != -1) {
            "ApiVersion[value=$value]"
        } else {
            "ApiVersion[$min-$max]"
        }
    }
}