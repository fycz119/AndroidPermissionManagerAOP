package com.example.app_permission_manager.annotation

import com.flyjingfish.android_aop_annotation.anno.AndroidAopPointCut
import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy

@AndroidAopPointCut(PermissionAOP::class)
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER
)
annotation class Permission(val value: Array<String>, val requestCode: Int){
}
