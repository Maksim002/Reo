package ru.ktsstudio.core_network_api.exceptions

import ru.ktsstudio.common.data.network.RetrofitException

class UnauthorizedException(errorMessage: String) : RetrofitException(errorMessage)