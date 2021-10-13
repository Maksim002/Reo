package ru.ktsstudio.app_verification.di.map

import dagger.Binds
import dagger.Module
import ru.ktsstudio.app_verification.data.map.ObjectMapRepositoryImpl
import ru.ktsstudio.app_verification.data.map.VerificationMapResourceProvider
import ru.ktsstudio.app_verification.presentation.object_filter.VerificationObjectTypeUiMapper
import ru.ktsstudio.feature_map.data.MapRepository
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import ru.ktsstudio.feature_map.data.MapResourceProvider

/**
 * Created by Igor Park on 30/09/2020.
 */
@Module
internal interface RecycleMapModule {

    @Binds
    fun bindsVerificationObjectTypeToStringMapper(
        impl: VerificationObjectTypeUiMapper
    ): Mapper<VerificationObjectType, String>

    @Binds
    @FeatureScope
    fun bindsObjectMapRepository(impl: ObjectMapRepositoryImpl): MapRepository

    @Binds
    @FeatureScope
    fun bindsMapResourceProvider(impl: VerificationMapResourceProvider): MapResourceProvider
}
