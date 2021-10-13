package ru.ktsstudio.feature_map.di

import dagger.BindsInstance
import dagger.Component
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.navigation.api.ModularNavigationApi
import ru.ktsstudio.common.navigation.api.RecycleMapNavigator
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_network_api.CoreNetworkApi
import ru.ktsstudio.feature_map.ui.MapFragment

@Component(
    dependencies = [
        CoreApi::class,
        CoreNetworkApi::class,
        RecycleMapFeatureDependency::class
    ],
    modules = [MapModule::class, MapFeatureModule::class]
)
@FeatureScope
interface RecycleMapFeatureComponent {
    fun inject(fragment: MapFragment)

    @Component.Factory
    interface Factory {
        fun create(
            coreNetworkApi: CoreNetworkApi,
            coreApi: CoreApi,
            mapFeatureDependency: RecycleMapFeatureDependency,
            @BindsInstance recycleMapNavigator: RecycleMapNavigator
        ): RecycleMapFeatureComponent
    }

    companion object {
        fun create(
            coreApi: CoreApi,
            coreNetworkApi: CoreNetworkApi,
            mapFeatureDependency: RecycleMapFeatureDependency
        ): RecycleMapFeatureComponent {
            return DaggerRecycleMapFeatureComponent.factory()
                .create(
                    coreApi = coreApi,
                    coreNetworkApi = coreNetworkApi,
                    mapFeatureDependency = mapFeatureDependency,
                    recycleMapNavigator = ComponentRegistry.get<ModularNavigationApi>()
                        .recycleMapNavigator()
                )
        }
    }
}
