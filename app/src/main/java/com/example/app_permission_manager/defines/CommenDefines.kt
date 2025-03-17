package com.example.app_permission_manager.defines

import com.example.app_permission_manager.interfaces.PermissionRequestCallback



object CommenDefines {
    const val REQUEST_PERMISSIONS: String = "request_permissions"

    //请求码
    const val REQUEST_CODE: String = "request_code"
    //默认请求码
    const val REQUEST_CODE_DEFAULT: Int = -1
    //回调接口
    var requestCallback: PermissionRequestCallback? = null
}