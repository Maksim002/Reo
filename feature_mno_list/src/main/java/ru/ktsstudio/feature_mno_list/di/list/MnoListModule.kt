package ru.ktsstudio.feature_mno_list.di.list

import androidx.lifecycle.ViewModel
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.ktsstudio.common.domain.filter.FilterUpdater
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.MnoRepository
import ru.ktsstudio.core_data_measurement_api.data.model.MnoFilterApplier
import ru.ktsstudio.feature_mno_list.domain.list.MnoListActor
import ru.ktsstudio.feature_mno_list.domain.list.MnoListFeature
import ru.ktsstudio.feature_mno_list.domain.list.MnoListReducer
import ru.ktsstudio.feature_mno_list.presentation.list.MnoListViewModel

/**
 * @author Maxim Ovchinnikov on 01.10.2020.
 */
@Module
internal interface MnoListModule {

    @Binds
    @IntoMap
    @ViewModelKey(MnoListViewModel::class)
    fun bindViewModel(impl: MnoListViewModel): ViewModel

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideMnoListFeature(
            mnoRepository: MnoRepository,
            measurementRepository: MeasurementRepository,
            schedulers: SchedulerProvider,
            filterUpdater: FilterUpdater,
            mnoFilterApplier: MnoFilterApplier
        ): Feature<MnoListFeature.Wish, MnoListFeature.State, Nothing> {
            return MnoListFeature(
                initialState = MnoListFeature.State(),
                actor = MnoListActor(
                    mnoRepository,
                    measurementRepository,
                    schedulers,
                    filterUpdater,
                    mnoFilterApplier
                ),
                reducer = MnoListReducer()
            )
        }

        @Provides
        @JvmStatic
        fun provideBinder(): Binder {
            return Binder()
        }
    }
}