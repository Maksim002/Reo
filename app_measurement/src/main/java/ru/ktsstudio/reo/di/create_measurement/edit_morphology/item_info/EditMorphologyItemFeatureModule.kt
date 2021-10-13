package ru.ktsstudio.reo.di.create_measurement.edit_morphology.item_info

import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.reo.domain.measurement.common.MorphologyDraftHolder
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.form.MeasurementForm
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.EditMorphologyItemActor
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.EditMorphologyItemFeature
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.EditMorphologyItemNewsPublisher
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.EditMorphologyItemReducer
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.MorphologyItemDraft
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.mappers.MorphologyDraftToItemMapper
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.mappers.MorphologyItemDraftMapper
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.mappers.MorphologyItemDraftToMeasurementFormMapper

/**
 * Created by Igor Park on 25/09/2020.
 */
@Module
internal interface EditMorphologyItemFeatureModule {
    @Binds
    fun bindDraftMapper(
        impl: MorphologyItemDraftMapper
    ): Mapper<MorphologyItem, MorphologyItemDraft>

    @Binds
    fun bindDraftToItemMapper(
        impl: MorphologyDraftToItemMapper
    ): Mapper2<MorphologyItemDraft, Long, MorphologyItem>

    @Binds
    fun bindDraftToFormMapper(
        impl: MorphologyItemDraftToMeasurementFormMapper
    ): Mapper<MorphologyItemDraft, MeasurementForm>

    companion object {
        @Provides
        fun provideInitialState(): EditMorphologyItemFeature.State {
            return EditMorphologyItemFeature.State(
                morphologyItemDraft = MorphologyItemDraft.getEmptyDraft(),
                requiredDataTypes = setOf(
                    ContainerDataType.WasteGroup,
                    ContainerDataType.WasteVolumeDaily,
                    ContainerDataType.WasteWeightDaily
                ),
                isLoading = true,
                error = null
            )
        }

        @Provides
        fun provideFeature(
            initialState: EditMorphologyItemFeature.State,
            measurementRepository: MeasurementRepository,
            morphologyItemToDraftMapper: Mapper<MorphologyItem, MorphologyItemDraft>,
            draftToMorphologyItemMapper: Mapper2<MorphologyItemDraft, Long, MorphologyItem>,
            draftToFormMapper: Mapper<MorphologyItemDraft, MeasurementForm>,
            schedulers: SchedulerProvider,
            draftHolder: MorphologyDraftHolder
        ): Feature<
            EditMorphologyItemFeature.Wish,
            EditMorphologyItemFeature.State,
            EditMorphologyItemFeature.News
            > {
            return EditMorphologyItemFeature(
                initialState = initialState,
                actor = EditMorphologyItemActor(
                    measurementRepository,
                    morphologyItemToDraftMapper,
                    draftToMorphologyItemMapper,
                    draftHolder,
                    schedulers
                ),
                reducer = EditMorphologyItemReducer(),
                newsPublisher = EditMorphologyItemNewsPublisher(draftToFormMapper)
            )
        }

        @Provides
        fun provideBinder(): Binder {
            return Binder()
        }
    }
}
