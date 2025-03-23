package com.example.app_permission_manager.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER
)
annotation class Permission(val value: Array<String>, val requestCode: Int){
}
