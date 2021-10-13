package ru.ktsstudio.core_network

import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.core_network_api.CoreNetworkApi
import ru.ktsstudio.core_network_impl.di.DaggerNetworkCoreComponent
import ru.ktsstudio.core_network_impl.di.DaggerNetworkCoreComponent_NetworkCoreDependenciesComponent

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.09.2020.
 */
object NetworkComponentFactory {

    fun create(
        coreApi: CoreApi,
        apiUrl: String,
        authApiUrl: String,
        clientId: String,
        clientSecret: String
    ): CoreNetworkApi {
        val deps = DaggerNetworkCoreComponent_NetworkCoreDependenciesComponent
            .factory()
            .create(
                coreApi = coreApi,
                apiUrl = apiUrl,
                authApiUrl = authApiUrl,
                clientId = clientId,
                clientSecret = clientSecret
            )
        return DaggerNetworkCoreComponent.builder()
            .networkCoreDependencies(deps)
            .build()
    }
}