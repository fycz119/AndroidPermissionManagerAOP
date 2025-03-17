package com.example.app_permission_manager.interfaces

interface PermissionRequestCallback {
    /**
     * 申请权限成功
     */
    fun permissionSuccess()

    /**
     * 申请权限失败，用户点击拒绝了
     */
    fun permissionCanceled()

    /**
     * 申请权限失败，用户点击了不再询问
     */
    fun permissionDenied()
}