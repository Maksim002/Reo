package ru.ktsstudio.feature_map.starter

import androidx.fragment.app.Fragment
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_network_api.CoreNetworkApi
import ru.ktsstudio.feature_map.di.RecycleMapFeatureComponent
import ru.ktsstudio.feature_map.di.RecycleMapFeatureDependency
import ru.ktsstudio.feature_map.ui.MapFragment

object RecycleMapFeatureStarter {
    fun start(
        coreApi: CoreApi,
        coreNetworkApi: CoreNetworkApi,
        dependency: RecycleMapFeatureDependency
    ): Fragment {
        RecycleMapFeatureComponent.create(coreApi, coreNetworkApi, dependency)
            .also { ComponentRegistry.register { it } }
        return MapFragment.getInstance()
    }
}
