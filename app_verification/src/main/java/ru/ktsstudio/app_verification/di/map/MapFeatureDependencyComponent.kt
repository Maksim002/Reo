package ru.ktsstudio.app_verification.di.map

import dagger.Subcomponent
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.feature_map.di.RecycleMapFeatureDependency

/**
 * @author Maxim Myalkin (MaxMyalkin) on 23.09.2020.
 */
@Subcomponent(
    modules = [RecycleMapModule::class]
)
@FeatureScope
internal interface MapFeatureDependencyComponent : RecycleMapFeatureDependency
