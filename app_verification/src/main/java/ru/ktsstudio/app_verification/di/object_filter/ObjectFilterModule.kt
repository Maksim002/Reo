package ru.ktsstudio.app_verification.di.object_filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.badoo.mvicore.binder.Binder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.ktsstudio.app_verification.domain.object_filter.ObjectFilterDataProvider
import ru.ktsstudio.app_verification.domain.object_filter.ObjectFilterItem
import ru.ktsstudio.app_verification.presentation.object_filter.ObjectFilterUiFieldToKeyMapper
import ru.ktsstudio.app_verification.presentation.object_filter.ObjectFilterUiStateTransformer
import ru.ktsstudio.app_verification.presentation.object_filter.VerificationObjectTypeUiMapper
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
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import javax.inject.Provider

/**
 * @author Maxim Ovchinnikov on 18.11.2020.
 */
@Module
internal interface ObjectFilterModule {

    @Binds
    fun bindMnoFilterDataProvider(
        impl: ObjectFilterDataProvider
    ): FilterDataProvider<ObjectFilterItem>

    @Binds
    fun bindsVerificationObjectTypeToStringMapper(
        impl: VerificationObjectTypeUiMapper
    ): Mapper<VerificationObjectType, String>

    companion object {

        @Provides
        @IntoMap
        @ViewModelKey(FilterViewModel::class)
        fun provideViewModel(
            filterUpdater: FilterUpdater,
            filterProvider: FilterProvider,
            filterDataProvider: FilterDataProvider<ObjectFilterItem>,
            schedulers: SchedulerProvider,
            verificationObjectTypeToStringMapper: Mapper<VerificationObjectType, String>
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
                fieldToKeyMapper = ObjectFilterUiFieldToKeyMapper(),
                uiStateTransformer = ObjectFilterUiStateTransformer(
                    ObjectFilterUiFieldToKeyMapper(),
                    verificationObjectTypeToStringMapper
                )
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
