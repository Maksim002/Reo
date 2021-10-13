package ru.ktsstudio.app_verification.di.object_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.ktsstudio.app_verification.domain.object_list.ObjectListActor
import ru.ktsstudio.app_verification.domain.object_list.ObjectListFeature
import ru.ktsstudio.app_verification.domain.object_list.ObjectListReducer
import ru.ktsstudio.app_verification.presentation.object_filter.VerificationObjectTypeUiMapper
import ru.ktsstudio.app_verification.presentation.object_list.ObjectListViewModel
import ru.ktsstudio.common.domain.filter.FilterUpdater
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectFilterApplier
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import javax.inject.Provider

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.11.2020.
 */
@Module
internal interface ObjectListModule {

    @Binds
    @IntoMap
    @ViewModelKey(ObjectListViewModel::class)
    fun bindViewModel(impl: ObjectListViewModel): ViewModel

    @Binds
    fun bindsVerificationObjectTypeToStringMapper(
        impl: VerificationObjectTypeUiMapper
    ): Mapper<VerificationObjectType, String>

    @Module
    companion object {

        @Provides
        fun provideViewModelProvider(
            viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
        ): ViewModelProvider.Factory {
            return ViewModelFactory(viewModelsMap)
        }

        @Provides
        fun provideObjectListFeature(
            objectRepository: ObjectRepository,
            schedulers: SchedulerProvider,
            filterUpdater: FilterUpdater,
            objectFilterApplier: VerificationObjectFilterApplier
        ): Feature<ObjectListFeature.Wish, ObjectListFeature.State, Nothing> {
            return ObjectListFeature(
                initialState = ObjectListFeature.State(),
                actor = ObjectListActor(objectRepository, schedulers, filterUpdater, objectFilterApplier),
                reducer = ObjectListReducer()
            )
        }

        @Provides
        fun provideBinder(): Binder {
            return Binder()
        }
    }
}
