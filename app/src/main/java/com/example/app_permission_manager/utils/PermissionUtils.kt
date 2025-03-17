package com.example.app_permission_manager.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.reflect.InvocationTargetException

object PermissionUtils {

    /**
     * 厂商设置映射表
     * Key: 厂商名称（小写） / Value: 对应的设置界面跳转类
     */
    // region 厂商常量定义
    private const val MANUFACTURER_DEFAULT = "default"  // 默认
    const val MANUFACTURER_HUAWEI = "huawei"           // 华为
    const val MANUFACTURER_MEIZU = "meizu"             // 魅族
    const val MANUFACTURER_XIAOMI = "xiaomi"           // 小米
    const val MANUFACTURER_SONY = "sony"               // 索尼
    const val MANUFACTURER_OPPO = "oppo"
    const val MANUFACTURER_LG = "lg"
    const val MANUFACTURER_VIVO = "vivo"
    const val MANUFACTURER_SAMSUNG = "samsung"         // 三星
    const val MANUFACTURER_LETV = "letv"               // 乐视
    const val MANUFACTURER_ZTE = "zte"                 // 中兴
    const val MANUFACTURER_YULONG = "yulong"           // 酷派
    const val MANUFACTURER_LENOVO = "lenovo"           // 联想

    private val permissionMenu = HashMap<String, Class<out ISetting>>().apply {
        put(MANUFACTURER_DEFAULT, DefaultStartSettings::class.java)
        put(MANUFACTURER_OPPO, OPPOStartSettings::class.java)
        put(MANUFACTURER_VIVO, VIVOStartSettings::class.java)
    }
    // endregion

    /**
     * 检查是否所有权限都已授予
     * @param context 上下文
     * @param permissions 需要检查的权限数组
     * @return 全部授予返回true
     */
    fun hasPermissionRequest(context: Context, vararg permissions: String): Boolean {
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * 判断权限请求结果是否全部成功
     * @param grantedResults 权限请求结果数组
     */
    fun requestPermissionSuccess(grantedResults: IntArray): Boolean {
        return grantedResults.isNotEmpty() && grantedResults.all {
            it == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * 检查用户是否勾选了"不再询问"
     * @return 存在至少一个权限勾选了"不再询问"返回true
     */
    fun shouldShowRequestPermissionRationale(activity: Activity, vararg permissions: String): Boolean {
        return permissions.any { permission ->
            !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
        }
    }

    /**
     * 反射执行带有指定注解的方法
     * @param target 目标对象
     * @param annotationClass 注解类
     */
    fun invokeAnnotation(target: Any, annotationClass: Class<out Annotation>) {
        target::class.java.declaredMethods.forEach { method ->
            method.isAccessible = true
            if (method.isAnnotationPresent(annotationClass)) {
                runCatching {
                    method.invoke(target)
                }.onFailure { e ->
                    when (e) {
                        is IllegalAccessException -> e.printStackTrace()
                        is InvocationTargetException -> e.printStackTrace()
                    }
                }
            }
        }
    }

    /**
     * 跳转到系统设置界面
     */
    fun startAndroidSettings(context: Context) {
        // 获取厂商对应的设置类
        val manufacturer = Build.MANUFACTURER.lowercase()
        val settingsClass = permissionMenu[manufacturer] ?: permissionMenu[MANUFACTURER_DEFAULT]

        settingsClass?.let {
            runCatching {
                val settings = it.getDeclaredConstructor().newInstance() as ISetting
                settings.getStartSettingsIntent(context)?.let { intent ->
                    context.startActivity(intent)
                }
            }.onFailure { e ->
                e.printStackTrace()
            }
        }
    }
}

// 接口定义（示例）
interface ISetting {
    fun getStartSettingsIntent(context: Context): Intent?
}

// 各厂商设置实现类（示例）
class DefaultStartSettings : ISetting {
    override fun getStartSettingsIntent(context: Context): Intent? = null
}

class OPPOStartSettings : ISetting {
    override fun getStartSettingsIntent(context: Context): Intent? = null
}

class VIVOStartSettings : ISetting {
    override fun getStartSettingsIntent(context: Context): Intent? = null
}
