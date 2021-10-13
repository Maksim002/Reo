package ru.ktsstudio.core_network_api.exceptions

import ru.ktsstudio.common.data.network.RetrofitException

class NotFoundException(errorMessage: String) : RetrofitException(errorMessage)