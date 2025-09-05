package com.example.test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * 测试示例应用程序入口类
 *
 * @author yang.zhang
 * @since 2025/9/5
 */
@SpringBootApplication
class TestExampleApplication {
    companion object {
        /**
         * 应用程序主入口方法
         *
         * @param args 命令行参数
         */
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<TestExampleApplication>(*args)
        }
    }
}