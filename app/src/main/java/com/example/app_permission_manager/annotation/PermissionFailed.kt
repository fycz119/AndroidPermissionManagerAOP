package com.example.app_permission_manager.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PermissionFailed(
    val requestCode: Int
)
