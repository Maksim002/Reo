package ru.ktsstudio.reo.di.measurement.filter

import androidx.lifecycle.ViewModel
import com.badoo.mvicore.binder.Binder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.ktsstudio.common.domain.filter.FilterActor
import ru.ktsstudio.common.domain.filter.FilterBootstrapper
import ru.ktsstudio.common.domain.filter.FilterFeature
import ru.ktsstudio.common.domain.filter.FilterNewsPublisher
import ru.ktsstudio.common.domain.filter.FilterProvider
import ru.ktsstudio.common.domain.filter.FilterReducer
import ru.ktsstudio.common.domain.filter.FilterUpdater
import ru.ktsstudio.common.domain.filter.data.FilterDataActor
import ru.ktsstudio.common.domain.filter.data.FilterDataFeature
import ru.ktsstudio.common.domain.filter.data.FilterDataProvider
import ru.ktsstudio.common.domain.filter.data.FilterDataReducer
import ru.ktsstudio.common.presentation.filter.FilterViewModel
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.reo.di.filter.MeasurementFilter
import ru.ktsstudio.reo.domain.measurement.MeasurementFilterDataProvider
import ru.ktsstudio.reo.domain.measurement.MeasurementFilterItem
import ru.ktsstudio.reo.presentation.measurement_filter.MeasurementFilterUiFieldToKeyMapper
import ru.ktsstudio.reo.presentation.measurement_filter.MeasurementFilterUiStateTransformer

/**
 * @author Maxim Myalkin (MaxMyalkin) on 20.10.2020.
 */
@Module
interface MeasurementFilterModule {

    @Binds
    fun bindMeasurementFilterDataProvider(
        impl: MeasurementFilterDataProvider
    ): FilterDataProvider<MeasurementFilterItem>

    companion object {

        @Provides
        @IntoMap
        @ViewModelKey(FilterViewModel::class)
        fun provideViewModel(
            @MeasurementFilter filterUpdater: FilterUpdater,
            @MeasurementFilter filterProvider: FilterProvider,
            filterDataProvider: FilterDataProvider<MeasurementFilterItem>,
            schedulers: SchedulerProvider
        ): ViewModel {
            return FilterViewModel(
                filterFeature = FilterFeature(
                    initialState = FilterFeature.State(),
                    actor = FilterActor(filterUpdater),
                    reducer = FilterReducer(),
                    newsPublisher = FilterNewsPublisher(),
                    filterBootstrapper = FilterBootstrapper(filterProvider, schedulers)
                ),
                filterDataFeature = FilterDataFeature(
                    initialState = FilterDataFeature.State(),
                    actor = FilterDataActor(filterDataProvider, schedulers),
                    reducer = FilterDataReducer()
                ),
                binder = Binder(),
                fieldToKeyMapper = MeasurementFilterUiFieldToKeyMapper(),
                uiStateTransformer = MeasurementFilterUiStateTransformer(MeasurementFilterUiFieldToKeyMapper())
            )
        }
    }
}
