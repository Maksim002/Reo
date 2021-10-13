package ru.ktsstudio.reo.di.create_measurement.edit_waste_type

import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.ktsstudio.common.utils.id.IdGenerator
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerWasteType
import ru.ktsstudio.reo.domain.measurement.common.WasteTypeDraftHolder
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.EditWasteTypeActor
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.EditWasteTypeFeature
import ru.ktsstudio.reo.domain.measurement.form.MeasurementForm
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.EditWasteTypeNewsPublisher
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.EditWasteTypeReducer
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.WasteTypeDraft
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.mappers.WasteTypeDraftMapper
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.mappers.WasteTypeDraftToMeasurementFormMapper
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.mappers.WasteTypeDraftToWasteTypeMapper

/**
 * Created by Igor Park on 25/09/2020.
 */
@Module
internal interface EditWasteTypeFeatureModule {

    @Binds
    fun bindDraftToFormMapper(
        impl: WasteTypeDraftToMeasurementFormMapper
    ): Mapper<WasteTypeDraft, MeasurementForm>

    @Binds
    fun bindWasteTypeToDraftMapper(
        impl: WasteTypeDraftMapper
    ): Mapper<ContainerWasteType, WasteTypeDraft>

    @Binds
    fun bindDraftToContainerWasteTypeMapper(
        impl: WasteTypeDraftToWasteTypeMapper
    ): Mapper2<WasteTypeDraft, String, ContainerWasteType>

    companion object {
        @Provides
        fun provideInitialState(): EditWasteTypeFeature.State {
            return EditWasteTypeFeature.State(
                wasteTypeDraft = WasteTypeDraft.getEmptyDraft(),
                requiredDataTypes = setOf(
                    ContainerDataType.WasteTypeCategory,
                    ContainerDataType.WasteTypeOtherCategoryName,
                    ContainerDataType.WasteVolume,
                    ContainerDataType.WasteVolumeDaily,
                    ContainerDataType.WasteWeight,
                    ContainerDataType.WasteWeightDaily
                ),
                isLoading = true,
                error = null
            )
        }

        @Provides
        fun provideFeature(
            initialState: EditWasteTypeFeature.State,
            measurementRepository: MeasurementRepository,
            wasteTypeToDraftMapper: Mapper<ContainerWasteType, WasteTypeDraft>,
            draftToWasteTypeMapper: Mapper2<WasteTypeDraft, String, ContainerWasteType>,
            draftToFormTypeMapper: Mapper<WasteTypeDraft, MeasurementForm>,
            schedulers: SchedulerProvider,
            draftHolder: WasteTypeDraftHolder,
            idGenerator: IdGenerator
        ): Feature<
            EditWasteTypeFeature.Wish,
            EditWasteTypeFeature.State,
            EditWasteTypeFeature.News
            > {
            return EditWasteTypeFeature(
                initialState = initialState,
                actor = EditWasteTypeActor(
                    measurementRepository,
                    wasteTypeToDraftMapper,
                    draftToWasteTypeMapper,
                    draftToFormTypeMapper,
                    schedulers,
                    draftHolder,
                    idGenerator
                ),
                reducer = EditWasteTypeReducer(),
                newsPublisher = EditWasteTypeNewsPublisher()
            )
        }

        @Provides
        fun provideBinder(): Binder {
            return Binder()
        }
    }
}
