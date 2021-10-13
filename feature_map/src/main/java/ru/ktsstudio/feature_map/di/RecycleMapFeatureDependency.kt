package ru.ktsstudio.feature_map.di

import ru.ktsstudio.feature_map.data.MapRepository
import ru.ktsstudio.feature_map.data.MapResourceProvider
import ru.ktsstudio.common.domain.filter.FilterProvider
import ru.ktsstudio.common.domain.filter.FilterUpdater

/**
 * Created by Igor Park on 30/09/2020.
 */
interface RecycleMapFeatureDependency {
    fun mapResourceProvider(): MapResourceProvider
    fun mapRepository(): MapRepository
    fun filterProvider(): FilterProvider
    fun filterUpdater(): FilterUpdater
}