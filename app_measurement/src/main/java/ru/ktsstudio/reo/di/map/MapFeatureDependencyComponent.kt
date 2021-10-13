package ru.ktsstudio.reo.di.map

import dagger.Subcomponent
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.domain.filter.FilterProvider
import ru.ktsstudio.common.domain.filter.FilterUpdater
import ru.ktsstudio.feature_map.data.MapRepository
import ru.ktsstudio.feature_map.data.MapResourceProvider
import ru.ktsstudio.feature_map.di.RecycleMapFeatureDependency
import ru.ktsstudio.reo.di.filter.MnoFilter

/**
 * @author Maxim Myalkin (MaxMyalkin) on 23.09.2020.
 */
@Subcomponent(
    modules = [RecycleMapModule::class]
)
@FeatureScope
internal interface MapFeatureDependencyComponent {
    fun mapRepository(): MapRepository
    fun mapResourceProvider(): MapResourceProvider
    @MnoFilter
    fun filterProvider(): FilterProvider
    @MnoFilter
    fun filterUpdater(): FilterUpdater
}

internal fun MapFeatureDependencyComponent.toRecycleMapFeatureDependency(): RecycleMapFeatureDependency {
    return object : RecycleMapFeatureDependency {
        override fun mapResourceProvider(): MapResourceProvider {
            return this@toRecycleMapFeatureDependency.mapResourceProvider()
        }

        override fun mapRepository(): MapRepository {
            return this@toRecycleMapFeatureDependency.mapRepository()
        }

        override fun filterProvider(): FilterProvider {
            return this@toRecycleMapFeatureDependency.filterProvider()
        }

        override fun filterUpdater(): FilterUpdater {
            return this@toRecycleMapFeatureDependency.filterUpdater()
        }
    }
}
