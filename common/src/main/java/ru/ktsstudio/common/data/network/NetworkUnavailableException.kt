package ru.ktsstudio.common.data.network

open class NetworkUnavailableException(
    error: String? = null,
    callInfo: String = ""
) : RuntimeException("${error ?: "network unavailable"} ${callInfo.isNotEmpty().let { "- $callInfo" }}")
