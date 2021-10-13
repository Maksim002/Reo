package ru.ktsstudio.core_network_impl.di

import dagger.BindsInstance
import dagger.Component
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.core_network_api.CoreNetworkApi
import ru.ktsstudio.core_network_api.qualifiers.ApiUrl
import ru.ktsstudio.core_network_api.qualifiers.AuthApiUrl
import ru.ktsstudio.core_network_api.qualifiers.ClientId
import ru.ktsstudio.core_network_api.qualifiers.ClientSecret
import ru.ktsstudio.common.di.GsonModule
import ru.ktsstudio.core_network_impl.di.modules.InterceptorModule
import ru.ktsstudio.core_network_impl.di.modules.OkHttpModule
import ru.ktsstudio.core_network_impl.di.modules.RetrofitModule

/**
 * @author Maxim Myalkin (MaxMyalkin) on 31.10.2019.
 */
@Component(
    dependencies = [NetworkCoreDependencies::class],
    modules = [
        InterceptorModule::class,
        OkHttpModule::class,
        RetrofitModule::class
    ]
)
@FeatureScope
interface NetworkCoreComponent : CoreNetworkApi {

    @Component(dependencies = [CoreApi::class])
    interface NetworkCoreDependenciesComponent : NetworkCoreDependencies {
        @Component.Factory
        interface Factory {
            fun create(
                coreApi: CoreApi,
                @ApiUrl @BindsInstance apiUrl: String,
                @AuthApiUrl @BindsInstance authApiUrl: String,
                @ClientId @BindsInstance clientId: String,
                @ClientSecret @BindsInstance clientSecret: String
            ): NetworkCoreDependenciesComponent
        }
    }
}
