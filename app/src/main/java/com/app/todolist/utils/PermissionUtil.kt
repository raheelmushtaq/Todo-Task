package com.app.todolist.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.intentfilter.androidpermissions.PermissionManager
import com.intentfilter.androidpermissions.models.DeniedPermissions
import java.util.Collections

object PermissionUtil {
    @RequiresApi(Build.VERSION_CODES.S)
    fun hasScheduleExactAlarmPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.SCHEDULE_EXACT_ALARM
        ) == PackageManager.PERMISSION_GRANTED
    }


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