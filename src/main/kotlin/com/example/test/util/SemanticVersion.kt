package com.example.test.util

/**
 * 语义化版本号类
 *
 * 用于解析和比较符合语义化版本规范（如0.0.1、1.0.1）的版本号
 */
data class SemanticVersion(
    val major: Int,    // 主版本号
    val minor: Int,    // 次版本号
    val patch: Int     // 修订号
) : Comparable<SemanticVersion> {
    companion object {
        /**
         * 从字符串解析语义化版本号
         *
         * @param version 版本字符串，格式为X.Y.Z
         * @return 解析后的SemanticVersion对象
         * @throws IllegalArgumentException 如果版本字符串格式不正确
         */
        fun parse(version: String): SemanticVersion {
            val parts = version.trim().split(".")
            if (parts.size != 3) {
                throw IllegalArgumentException("Invalid semantic version format: $version. Expected format: X.Y.Z")
            }

            try {
                val major = parts[0].toInt()
                val minor = parts[1].toInt()
                val patch = parts[2].toInt()

                if (major < 0 || minor < 0 || patch < 0) {
                    throw IllegalArgumentException("Version components must be non-negative integers")
                }

                return SemanticVersion(major, minor, patch)
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("Invalid semantic version format: $version. All components must be integers")
            }
        }
    }

    /**
     * 比较两个语义化版本号
     *
     * @param other 要比较的另一个版本号
     * @return 负数表示当前版本小于other，正数表示当前版本大于other，0表示相等
     */
    override fun compareTo(other: SemanticVersion): Int {
        if (this.major != other.major) {
            return this.major - other.major
        }
        if (this.minor != other.minor) {
            return this.minor - other.minor
        }
        return this.patch - other.patch
    }

    /**
     * 将版本号转换为字符串表示
     *
     * @return 格式为"major.minor.patch"的字符串
     */
    override fun toString(): String {
        return "$major.$minor.$patch"
    }
}