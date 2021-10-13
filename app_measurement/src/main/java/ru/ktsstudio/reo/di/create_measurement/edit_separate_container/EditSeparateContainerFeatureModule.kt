package ru.ktsstudio.reo.di.create_measurement.edit_separate_container

import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Module
import dagger.Provides
import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.MnoRepository
import ru.ktsstudio.core_data_measurement_api.domain.SeparateContainerComposite
import ru.ktsstudio.reo.domain.measurement.common.SeparateContainerDraftHolder
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.edit_separate_container.EditSeparateContainerActor
import ru.ktsstudio.reo.domain.measurement.edit_separate_container.EditSeparateContainerFeature
import ru.ktsstudio.reo.domain.measurement.edit_separate_container.EditSeparateContainerNewsPublisher
import ru.ktsstudio.reo.domain.measurement.edit_separate_container.EditSeparateContainerReducer

/**
 * Created by Igor Park on 25/09/2020.
 */
@Module
internal interface EditSeparateContainerFeatureModule {

    companion object {
        @Provides
        fun provideInitialState(): EditSeparateContainerFeature.State {
            return EditSeparateContainerFeature.State(
                separateContainer = SeparateContainerComposite(
                    localId = 0L,
                    isUnique = false,
                    uniqueName = null,
                    uniqueVolume = null,
                    containerType = null,
                    mnoContainer = null,
                    wasteTypes = emptyList(),
                    draftState = DraftState.IDLE
                ),
                requiredDataTypes = setOf(ContainerDataType.ContainerName),
                isLoading = true,
                error = null
            )
        }

        @Provides
        fun provideFeature(
            initialState: EditSeparateContainerFeature.State,
            measurementRepository: MeasurementRepository,
            mnoRepository: MnoRepository,
            schedulers: SchedulerProvider,
            draftHolder: SeparateContainerDraftHolder
        ): Feature<
            EditSeparateContainerFeature.Wish,
            EditSeparateContainerFeature.State,
            EditSeparateContainerFeature.News
            > {
            return EditSeparateContainerFeature(
                initialState = initialState,
                actor = EditSeparateContainerActor(
                    measurementRepository,
                    mnoRepository,
                    schedulers,
                    draftHolder
                ),
                reducer = EditSeparateContainerReducer(),
                newsPublisher = EditSeparateContainerNewsPublisher()
            )
        }

        @Provides
        fun provideBinder(): Binder {
            return Binder()
        }
    }
}
