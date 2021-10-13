package ru.ktsstudio.reo.di.create_measurement

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.reo.domain.measurement.common.DraftHolder
import ru.ktsstudio.reo.domain.measurement.common.MeasurementDraftHolder
import ru.ktsstudio.reo.domain.measurement.common.MixedContainerDraftHolder
import ru.ktsstudio.reo.domain.measurement.common.MorphologyDraftHolder
import ru.ktsstudio.reo.domain.measurement.common.SeparateContainerDraftHolder
import ru.ktsstudio.reo.domain.measurement.common.WasteTypeDraftHolder

/**
 * Created by Igor Park on 18/11/2020.
 */
@Module
interface DraftHolderModule {
    @Binds
    fun bindsMeasurementDraftHolder(impl: DraftHolder): MeasurementDraftHolder

    @Binds
    fun bindsMixedContainerDraftHolder(impl: DraftHolder): MixedContainerDraftHolder

    @Binds
    fun bindsSeparateContainerDraftHolder(impl: DraftHolder): SeparateContainerDraftHolder

    @Binds
    fun bindsMorphologyDraftHolder(impl: DraftHolder): MorphologyDraftHolder

    @Binds
    fun bindsWasteTypeDraftHolder(impl: DraftHolder): WasteTypeDraftHolder

    companion object {
        @Provides
        @FeatureScope
        fun provideDraftHolder(): DraftHolder = DraftHolder()
    }
}
