package com.app.todolist.utils

import android.content.Context
import com.intentfilter.androidpermissions.PermissionManager
import com.intentfilter.androidpermissions.models.DeniedPermissions
import java.util.Collections

/*
permission utils is used to request permission frmo user*/
object PermissionUtil {
    //this functions user takes context, and permissioon to request and reqturns the success of railure status of the request.
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