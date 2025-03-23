package com.example.app_permission_manager.annotation

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.app_permission_manager.defines.CommenDefines.TAG
import com.example.app_permission_manager.interfaces.PermissionRequestCallback
import com.example.app_permission_manager.transparent.ApplyPermissionActivity
import com.example.app_permission_manager.utils.PermissionUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

@Aspect
class PermissionAOP {

    @Pointcut("execution(@com.example.app_permission_manager.annotation.Permission * *(..)) && @annotation(permissions)")
    fun requestPermission(permissions: Permission) {
        Log.d(TAG, "requestPermission: ")
    }

    @Around("requestPermission(permissions)")
    fun getPermissionPoint(joinPoint: ProceedingJoinPoint, permissions: Permission): Any? {
        try {
            Log.d(TAG, "getPermissionPoint: ")


            //获取到上下文
            var context: Context? = null

            //获取到注解所在的类对象
            val aThis: Any = joinPoint.args[0]

            //判断aThis是否是Context的子类  如果是 就进行赋值
            if (aThis is Context) {
                context = aThis
            } else if (aThis is Fragment) {
                context = (aThis as Fragment).getActivity()
            }

            //获取到要申请的权限
            if (context == null || permissions == null || permissions.value == null || permissions.value.count() === 0) {
                return null
            }

            val value = permissions.value
            val requestCode = permissions.requestCode
            var result: Any? = null // 新增变量存储执行结果


            ApplyPermissionActivity.launchActivity(
                context,
                value,
                requestCode,
                object : PermissionRequestCallback {
                    override fun permissionSuccess() {
                        Log.i("Permission", "权限申请成功: ${value.joinToString()}")
                        try {
                            result = joinPoint.proceed() // 存储执行结果
                        } catch (e: Throwable) {
                            Log.e("Permission", "方法执行异常", e)
                            result = null // 异常时返回null
                        }
                    }

                    override fun permissionCanceled() {
                        PermissionUtils.invokeAnnotation(context, PermissionFailed::class.java)
                        Log.w("Permission", "权限被拒绝: ${value.getOrNull(0)}")
                    }

                    override fun permissionDenied() {
                        PermissionUtils.invokeAnnotation(context, PermissionDenied::class.java)
                        Log.e("Permission", "权限被永久拒绝: ${value.joinToString()}")
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