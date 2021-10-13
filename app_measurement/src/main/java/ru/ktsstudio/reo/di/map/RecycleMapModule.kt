package ru.ktsstudio.reo.di.map

import dagger.Binds
import dagger.Module
import ru.ktsstudio.feature_map.data.MapRepository
import ru.ktsstudio.feature_map.data.MapResourceProvider
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.reo.data.map.MnoMapRepositoryImpl
import ru.ktsstudio.reo.data.map.MnoMapResourceProvider

/**
 * Created by Igor Park on 30/09/2020.
 */
@Module
internal interface RecycleMapModule {

    @Binds
    @FeatureScope
    fun bindsLocationRepository(impl: MnoMapRepositoryImpl): MapRepository

    @Binds
    @FeatureScope
    fun bindsMapResourceProvider(impl: MnoMapResourceProvider): MapResourceProvider
}
