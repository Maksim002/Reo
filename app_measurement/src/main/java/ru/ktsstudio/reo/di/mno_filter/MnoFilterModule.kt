package ru.ktsstudio.reo.di.mno_filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.reo.di.filter.MnoFilter
import ru.ktsstudio.reo.domain.mno_filter.MnoFilterDataProvider
import ru.ktsstudio.reo.domain.mno_filter.MnoFilterItem
import ru.ktsstudio.reo.presentation.mno_filter.MnoFilterUiFieldToKeyMapper
import ru.ktsstudio.reo.presentation.mno_filter.MnoFilterUiStateTransformer
import javax.inject.Provider

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
@Module
internal abstract class MnoFilterModule {

    @Binds
    abstract fun bindMnoFilterDataProvider(
        impl: MnoFilterDataProvider
    ): FilterDataProvider<MnoFilterItem>

    companion object {

        @Provides
        @IntoMap
        @ViewModelKey(FilterViewModel::class)
        fun provideViewModel(
            @MnoFilter filterUpdater: FilterUpdater,
            @MnoFilter filterProvider: FilterProvider,
            filterDataProvider: FilterDataProvider<MnoFilterItem>,
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
                fieldToKeyMapper = MnoFilterUiFieldToKeyMapper(),
                uiStateTransformer = MnoFilterUiStateTransformer(MnoFilterUiFieldToKeyMapper())
            )
        }

        @Provides
        fun provideViewModelProvider(
            viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
        ): ViewModelProvider.Factory {
            return ViewModelFactory(viewModelsMap)
        }
    }
}
