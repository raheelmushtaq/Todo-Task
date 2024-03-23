package com.app.todolist.utils

import android.content.Context
import com.intentfilter.androidpermissions.PermissionManager
import com.intentfilter.androidpermissions.models.DeniedPermissions
import java.util.Collections

object PermissionUtil {
    fun requestPermission(
        context: Context,
        permission: String,
        onResult: (Boolean) -> Unit,
    ) {
        val permissionManager = PermissionManager.getInstance(context)
        permissionManager.checkPermissions(
            Collections.singleton(permission),
            object : PermissionManager.PermissionRequestListener {
                override fun onPermissionGranted() {
                    onResult.invoke(true)
                }

                override fun onPermissionDenied(deniedPermissions: DeniedPermissions) {
                    onResult.invoke(false)
                }
            })
    }
}