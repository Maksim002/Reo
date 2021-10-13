package ru.ktsstudio.core_network_api.exceptions

import ru.ktsstudio.common.data.network.RetrofitException

class BadRequestException(errorMessage: String) : RetrofitException(errorMessage)