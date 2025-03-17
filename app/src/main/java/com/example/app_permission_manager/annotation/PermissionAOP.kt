package com.example.app_permission_manager.annotation

import android.content.Context
import android.util.Log
import com.example.app_permission_manager.interfaces.PermissionRequestCallback
import com.example.app_permission_manager.transparent.ApplyPermissionActivity
import com.example.app_permission_manager.utils.PermissionUtils
import com.flyjingfish.android_aop_annotation.ProceedJoinPoint
import com.flyjingfish.android_aop_annotation.base.BasePointCut

class PermissionAOP: BasePointCut<Permission> {
    override fun invoke(joinPoint: ProceedJoinPoint, anno: Permission): Any? {
        val permissions = anno.value
        var context = joinPoint.args?.get(0) as Context
        ApplyPermissionActivity.launchActivity(
            context,
            permissions.value,
            permissions.requestCode(),
            object : PermissionRequestCallback {
                override fun permissionSuccess() {
                    Log.i("Permission", "权限申请成功: ${permissions.value?.joinToString()}")
                    try {
                        joinPoint.proceed()
                    } catch (e: Throwable) {
                        Log.e("Permission", "方法执行异常", e)
                    }
                }

                override fun permissionCanceled() {
                    val target = joinPoint.`this`
                    if (target != null) {
                        PermissionUtils.invokeAnnotation(target, PermissionFailed::class.java)
                        Log.w("Permission", "权限被拒绝: ${permissions.value?.getOrNull(0)}")
                    } else {
                        Log.e("Permission", "目标对象不存在")
                    }
                }

                override fun permissionDenied() {
                    val target = joinPoint.`this`
                    if (target != null) {
                        PermissionUtils.invokeAnnotation(target, PermissionDenied::class.java)
                        Log.e("Permission", "权限被永久拒绝: ${permissions.value?.joinToString()}")
                    } else {
                        Log.w("Permission", "上下文丢失无法处理永久拒绝")
                    }
                }
            }
        )

    }
}