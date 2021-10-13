package ru.ktsstudio.feature_auth.di.flow

import dagger.BindsInstance
import dagger.Component
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.di.CoreAuthApi
import ru.ktsstudio.common.navigation.api.AuthNavigator
import ru.ktsstudio.common.navigation.api.ModularNavigationApi
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_network_api.CoreNetworkApi
import ru.ktsstudio.feature_auth.di.flow.modules.AuthModule

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.09.2020.
 */
@Component(
    dependencies = [
        CoreApi::class,
        CoreNetworkApi::class,
        CoreAuthApi::class
    ],
    modules = [AuthModule::class]
)
internal interface AuthFlowComponent {
    fun checkAuthComponent(): CheckAuthComponent
    fun loginComponent(): LoginComponent

    @Component.Factory
    interface Factory {
        fun create(
            coreApi: CoreApi,
            coreNetworkApi: CoreNetworkApi,
            coreAuthApi: CoreAuthApi,
            @BindsInstance authNavigator: AuthNavigator,
        ): AuthFlowComponent
    }

    companion object {
        fun create(): AuthFlowComponent {
            return DaggerAuthFlowComponent.factory()
                .create(
                    coreApi = ComponentRegistry.get(),
                    coreNetworkApi = ComponentRegistry.get(),
                    coreAuthApi = ComponentRegistry.get(),
                    authNavigator = ComponentRegistry.get<ModularNavigationApi>()
                        .authNavigator()
                )
        }
    }
}