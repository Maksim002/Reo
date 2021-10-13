package ru.ktsstudio.core_data_verification

import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.core_data_verfication_api.di.CoreVerificationDataApi
import ru.ktsstudio.core_data_verification_impl.di.DataCoreVerificationComponent
import ru.ktsstudio.core_network_api.CoreNetworkApi

object CoreDataComponentFactory {

    fun create(
        coreApi: CoreApi,
        coreNetworkApi: CoreNetworkApi,
        authority: String
    ): CoreVerificationDataApi {
        return DataCoreVerificationComponent.create(coreApi, coreNetworkApi, authority)
    }
}