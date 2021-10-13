package ru.ktsstudio.reo.di.create_measurement.edit_mixed_container

import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.MnoRepository
import ru.ktsstudio.core_data_measurement_api.domain.MixedWasteContainerComposite
import ru.ktsstudio.reo.domain.measurement.common.MixedContainerDraftHolder
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.EditMixedContainerActor
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.EditMixedContainerFeature
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.EditMixedContainerNewsPublisher
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.EditMixedContainerReducer
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.mappers.MixedWasteContainerDraftToMeasurementFormMapper
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.mappers.MixedContainerDraftToCompositeMapper
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.mappers.MixedWasteContainerDraftMapper
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.MixedWasteContainerDraft
import ru.ktsstudio.reo.domain.measurement.form.MeasurementForm

/**
 * Created by Igor Park on 25/09/2020.
 */
@Module
internal interface EditMixedContainerFeatureModule {

    @Binds
    fun bindMixedCompositeContainerToDraftMapper(
        impl: MixedWasteContainerDraftMapper
    ): Mapper<MixedWasteContainerComposite, MixedWasteContainerDraft>

    @Binds
    fun bindDraftToMixedCompositeContainerMapper(
        impl: MixedContainerDraftToCompositeMapper
    ): Mapper2<MixedWasteContainerDraft, Long, MixedWasteContainerComposite>

    @Binds
    fun bindDraftToMeasurementFormMapper(
        impl: MixedWasteContainerDraftToMeasurementFormMapper
    ): Mapper<MixedWasteContainerDraft, MeasurementForm>

    companion object {
        @Provides
        fun provideInitialState(): EditMixedContainerFeature.State {
            return EditMixedContainerFeature.State(
                containerDraft = MixedWasteContainerDraft.getEmptyDraft(),
                requiredDataTypes = setOf(
                    ContainerDataType.ContainerName,
                    ContainerDataType.WasteWeight,
                    ContainerDataType.WasteWeightDaily,
                    ContainerDataType.WasteVolume,
                    ContainerDataType.WasteVolumeDaily
                ),
                isLoading = true,
                isUpdating = false,
                error = null
            )
        }

        @Provides
        fun provideFeature(
            initialState: EditMixedContainerFeature.State,
            measurementRepository: MeasurementRepository,
            mnoRepository: MnoRepository,
            draftHolder: MixedContainerDraftHolder,
            mixedCompositeContainerToDraftMapper: Mapper<
                MixedWasteContainerComposite,
                MixedWasteContainerDraft
                >,
            draftToMixedCompositeContainerMapper: Mapper2<
                MixedWasteContainerDraft,
                Long,
                MixedWasteContainerComposite
                >,
            draftToMeasurementFormMapper: Mapper<MixedWasteContainerDraft, MeasurementForm>,
            schedulers: SchedulerProvider
        ): Feature<
            EditMixedContainerFeature.Wish,
            EditMixedContainerFeature.State,
            EditMixedContainerFeature.News
            > {
            return EditMixedContainerFeature(
                initialState = initialState,
                actor = EditMixedContainerActor(
                    measurementRepository,
                    mnoRepository,
                    mixedCompositeContainerToDraftMapper,
                    draftToMixedCompositeContainerMapper,
                    draftToMeasurementFormMapper,
                    schedulers,
                    draftHolder
                ),
                reducer = EditMixedContainerReducer(),
                newsPublisher = EditMixedContainerNewsPublisher()
            )
        }

        @Provides
        fun provideBinder(): Binder {
            return Binder()
        }
    }
}
