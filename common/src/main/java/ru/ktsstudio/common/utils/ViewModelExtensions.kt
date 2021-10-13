package ru.ktsstudio.common.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe

/**
 * Created by Igor Park on 23/03/2020.
 */
fun <T : Any?, L : LiveData<T>> LifecycleOwner.observeNotNull(liveData: L, body: (T) -> Unit) {
    liveData.observe(this) { if (it != null) body(it) }
}

fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }
