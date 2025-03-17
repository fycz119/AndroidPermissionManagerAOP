package com.example.app_permission_manager.transparent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.app_permission_manager.interfaces.PermissionRequestCallback
import com.example.app_permission_manager.utils.PermissionUtils


class ApplyPermissionActivity: AppCompatActivity() {
    companion object {
        private const val EXTRA_PERMISSIONS = "extra_permissions"
        private const val EXTRA_REQUEST_CODE = "extra_request_code"

        private var requestCallback: PermissionRequestCallback? = null


        fun launchActivity(
            context: Context,
            permissions: Array<String>,
            requestCode: Int = -1,
            requestCallback: PermissionRequestCallback
        ) {
            this.requestCallback = requestCallback

            context.startActivity(Intent(context, ApplyPermissionActivity::class.java).apply {
                putExtra(EXTRA_PERMISSIONS, permissions)
                putExtra(EXTRA_REQUEST_CODE, requestCode)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.extras?.let { bundle ->
            val permissions = bundle.getStringArray(EXTRA_PERMISSIONS)
            val requestCode = bundle.getInt(EXTRA_REQUEST_CODE, -1)

            when {
                permissions == null || requestCode == -1 || requestCallback == null -> finish()
                PermissionUtils.hasPermissionRequest(this, *permissions) -> requestCallback!!.permissionSuccess()
                else -> requestPermissions(permissions, requestCode)
            }
        } ?: finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when {
            PermissionUtils.requestPermissionSuccess(grantResults) -> handleSuccess()
            PermissionUtils.shouldShowRequestPermissionRationale(this, *permissions) -> handleDenied()
            else -> handleCanceled()
        }
        finish()
    }

    private fun handleSuccess() {
        requestCallback?.permissionSuccess()
        requestCallback = null
    }

    private fun handleDenied() {
        requestCallback?.permissionDenied()
        requestCallback = null
    }

    private fun handleCanceled() {
        requestCallback?.permissionCanceled()
        requestCallback = null
    }
}