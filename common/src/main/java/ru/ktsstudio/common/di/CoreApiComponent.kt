package ru.ktsstudio.common.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.ktsstudio.common.app.AppCodeProvider
import ru.ktsstudio.common.data.AppVersion
import ru.ktsstudio.common.navigation.api.ModularNavigationApi

/**
 * @author Maxim Myalkin (MaxMyalkin) on 23.09.2020.
 */
@Component(
    dependencies = [CoreApiDependencies::class],
    modules = [CoreApiModule::class, LocationModule::class, GsonModule::class]
)
@FeatureScope
interface CoreApiComponent : CoreApi {

    @Component.Factory
    interface Factory {
        fun create(
            dependencies: CoreApiDependencies
        ): CoreApiComponent
    }

    @Component
    interface CoreApiDependenciesComponent : CoreApiDependencies {
        @Component.Factory
        interface Factory {
            fun create(
                @BindsInstance context: Context,
                @BindsInstance appCodeProvider: AppCodeProvider,
                @BindsInstance @AppVersion appVersion: String
            ): CoreApiDependenciesComponent
        }
    }

    companion object {
        fun create(
            context: Context,
            appCodeProvider: AppCodeProvider,
            appVersion: String
        ): CoreApi {
            val deps = DaggerCoreApiComponent_CoreApiDependenciesComponent.factory()
                .create(context, appCodeProvider, appVersion)
            return DaggerCoreApiComponent.factory().create(deps)
        }
    }
}