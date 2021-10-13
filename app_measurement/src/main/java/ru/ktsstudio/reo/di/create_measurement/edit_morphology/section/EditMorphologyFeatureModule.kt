package ru.ktsstudio.reo.di.create_measurement.edit_morphology.section

import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Module
import dagger.Provides
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.reo.domain.measurement.common.MorphologyDraftHolder
import ru.ktsstudio.reo.domain.measurement.morphology.section.EditMorphologyActor
import ru.ktsstudio.reo.domain.measurement.morphology.section.EditMorphologyFeature
import ru.ktsstudio.reo.domain.measurement.morphology.section.EditMorphologyReducer

/**
 * Created by Igor Park on 25/09/2020.
 */
@Module
internal interface EditMorphologyFeatureModule {

    companion object {
        @Provides
        fun provideInitialState(): EditMorphologyFeature.State {
            return EditMorphologyFeature.State(
                morphologyList = emptyList(),
                isLoading = true,
                error = null
            )
        }

        @Provides
        fun provideFeature(
            initialState: EditMorphologyFeature.State,
            measurementRepository: MeasurementRepository,
            schedulers: SchedulerProvider,
            draftHolder: MorphologyDraftHolder
        ): Feature<
            EditMorphologyFeature.Wish,
            EditMorphologyFeature.State,
            Nothing
            > {
            return EditMorphologyFeature(
                initialState = initialState,
                actor = EditMorphologyActor(
                    measurementRepository,
                    schedulers,
                    draftHolder
                ),
                reducer = EditMorphologyReducer()
            )
        }

        @Provides
        fun provideBinder(): Binder {
            return Binder()
        }
    }
}
