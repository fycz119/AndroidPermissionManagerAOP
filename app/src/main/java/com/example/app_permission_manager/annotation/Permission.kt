package com.example.app_permission_manager.annotation

import com.flyjingfish.android_aop_annotation.anno.AndroidAopPointCut

@AndroidAopPointCut(PermissionAOP::class)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Permission(val value: Array<String>, val requestCode: Int){
}
