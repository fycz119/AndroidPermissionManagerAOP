package com.example.app_permission_manager.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.BuildCompat
import androidx.core.net.toUri

object SystemSettingUtils {
    // 使用 Activity Result API 封装跳转逻辑（可选）
    fun registerSettingsLauncher(activity: AppCompatActivity, onResult: () -> Unit) : ActivityResultLauncher<Intent> {
        return activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // 当从设置页面返回时触发回调
            onResult()
        }
    }

    // 构建跳转到应用设置的 Intent
    fun buildAppSettingsIntent(context: Context): Intent {
        val packageName = "package:" + "com.example.app_permission_manager" // 动态获取包名
        return Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            "package:$packageName".toUri()
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    // 扩展函数方式直接跳转
    fun Context.openAppSettings() {
        try {
            startActivity(buildAppSettingsIntent(this))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "无法打开系统设置", Toast.LENGTH_SHORT).show()
        }
    }
}