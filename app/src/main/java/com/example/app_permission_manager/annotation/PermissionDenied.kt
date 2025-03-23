package com.example.app_permission_manager.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PermissionDenied(
    /**
     * 请求码
     */
    val requestCode: Int
)