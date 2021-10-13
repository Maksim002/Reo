package ru.ktsstudio.common.ui.permissions

import permissions.dispatcher.PermissionRequest
import java.lang.ref.WeakReference

internal typealias Fun = () -> Unit
internal typealias ShowRationaleFun = (PermissionRequest) -> Unit

class KtxPermissionRequest(
    private val requestPermission: WeakReference<Fun>,
    private val permissionDenied: WeakReference<Fun>?
) : PermissionRequest {
    override fun proceed() {
        requestPermission.get()?.invoke()
    }

    override fun cancel() {
        permissionDenied?.get()?.invoke()
    }

    companion object {
        fun create(onPermissionDenied: Fun?, requestPermission: Fun) = KtxPermissionRequest(
            requestPermission = WeakReference(requestPermission),
            permissionDenied = onPermissionDenied?.let { WeakReference(it) }
        )
    }
}
