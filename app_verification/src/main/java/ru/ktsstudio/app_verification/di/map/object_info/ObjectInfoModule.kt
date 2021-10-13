package ru.ktsstudio.app_verification.di.map.object_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.ktsstudio.app_verification.domain.map.object_info.ObjectInfoActor
import ru.ktsstudio.app_verification.domain.map.object_info.ObjectInfoFeature
import ru.ktsstudio.app_verification.domain.map.object_info.ObjectInfoNewsPublisher
import ru.ktsstudio.app_verification.domain.map.object_info.ObjectInfoReducer
import ru.ktsstudio.app_verification.presentation.map.ObjectInfoViewModel
import ru.ktsstudio.app_verification.presentation.object_filter.VerificationObjectTypeUiMapper
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import ru.ktsstudio.common.data.location.LocationRepository
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import javax.inject.Provider

@Module
interface ObjectInfoModule {

    @Binds
    @IntoMap
    @ViewModelKey(ObjectInfoViewModel::class)
    fun bindViewModel(impl: ObjectInfoViewModel): ViewModel

    @Binds
    fun bindsVerificationObjectTypeToStringMapper(
        impl: VerificationObjectTypeUiMapper
    ): Mapper<VerificationObjectType, String>

    companion object {
        @Provides
        @JvmStatic
        fun provideViewModelProvider(
            viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
        ): ViewModelProvider.Factory {
            return ViewModelFactory(viewModelsMap)
        }

        @Provides
        @JvmStatic
        fun provideMapFeature(
            objectRepository: ObjectRepository,
            locationRepository: LocationRepository,
            schedulers: SchedulerProvider
        ): Feature<
            ObjectInfoFeature.Wish,
            ObjectInfoFeature.State,
            ObjectInfoFeature.News
            > {
            return ObjectInfoFeature(
                initialState = ObjectInfoFeature.State(
                    isLoading = false,
                    error = null,
                    infoList = emptyList(),
                    destination = null
                ),
                actor = ObjectInfoActor(objectRepository, locationRepository, schedulers),
                reducer = ObjectInfoReducer(),
                newsPublisher = ObjectInfoNewsPublisher()
            )
        }

        @Provides
        @JvmStatic
        fun provideBinder(): Binder {
            return Binder()
        }
    }
}
