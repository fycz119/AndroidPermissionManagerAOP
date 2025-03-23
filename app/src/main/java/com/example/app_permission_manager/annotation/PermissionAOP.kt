package com.example.app_permission_manager.annotation

import android.content.Context
import android.util.Log
import com.example.app_permission_manager.defines.CommenDefines.TAG
import com.example.app_permission_manager.interfaces.PermissionRequestCallback
import com.example.app_permission_manager.transparent.ApplyPermissionActivity
import com.example.app_permission_manager.utils.PermissionUtils
import com.flyjingfish.android_aop_annotation.ProceedJoinPoint
import com.flyjingfish.android_aop_annotation.base.BasePointCut

class PermissionAOP: BasePointCut<Permission> {
    override fun invoke(joinPoint: ProceedJoinPoint, anno: Permission): Any? {
        try {
            Log.d(TAG, "invoke: ")
            val permissions = anno.value
            val requestCode = anno.requestCode
            val context = joinPoint.args?.get(0) as Context
            var result: Any? = null // 新增变量存储执行结果


            ApplyPermissionActivity.launchActivity(
                context,
                permissions,
                requestCode,
                object : PermissionRequestCallback {
                    override fun permissionSuccess() {
                        Log.i("Permission", "权限申请成功: ${permissions.joinToString()}")
                        try {
                            result = joinPoint.proceed() // 存储执行结果
                        } catch (e: Throwable) {
                            Log.e("Permission", "方法执行异常", e)
                            result = null // 异常时返回null
                        }
                    }

                    override fun permissionCanceled() {
                        PermissionUtils.invokeAnnotation(context, PermissionFailed::class.java)
                        Log.w("Permission", "权限被拒绝: ${permissions.getOrNull(0)}")
                    }

                    override fun permissionDenied() {
                        PermissionUtils.invokeAnnotation(context, PermissionDenied::class.java)
                        Log.e("Permission", "权限被永久拒绝: ${permissions.joinToString()}")
                    }
                }
            )
            return result // 返回存储的结果
        } catch (e: Exception) {
            Log.e(TAG, "invoke: " + e.message)
            Log.e(TAG, "invoke: " + e.printStackTrace())
        }
        return null
    }
}