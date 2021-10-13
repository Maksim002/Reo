package ru.ktsstudio.app_verification.di.object_inspection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.ktsstudio.app_verification.domain.object_inspection.ObjectInspectionActor
import ru.ktsstudio.app_verification.domain.object_inspection.ObjectInspectionFeature
import ru.ktsstudio.app_verification.domain.object_inspection.ObjectInspectionNewsPublisher
import ru.ktsstudio.app_verification.domain.object_inspection.ObjectInspectionReducer
import ru.ktsstudio.app_verification.presentation.object_inspection.ObjectInspectionViewModel
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import javax.inject.Provider

/**
 * @author Maxim Ovchinnikov on 21.11.2020.
 */
@Module
internal interface ObjectInspectionModule {

    @Binds
    @IntoMap
    @ViewModelKey(ObjectInspectionViewModel::class)
    fun bindViewModel(impl: ObjectInspectionViewModel): ViewModel

    @Module
    companion object {

        @Provides
        fun provideViewModelProvider(
            viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
        ): ViewModelProvider.Factory {
            return ViewModelFactory(viewModelsMap)
        }

        @Provides
        fun provideObjectInspectionFeature(
            objectRepository: ObjectRepository,
            schedulers: SchedulerProvider
        ): Feature<ObjectInspectionFeature.Wish, ObjectInspectionFeature.State, ObjectInspectionFeature.News> {
            return ObjectInspectionFeature(
                initialState = ObjectInspectionFeature.State(),
                actor = ObjectInspectionActor(objectRepository, schedulers),
                reducer = ObjectInspectionReducer(),
                newsPublisher = ObjectInspectionNewsPublisher()
            )
        }

        @Provides
        fun provideBinder(): Binder {
            return Binder()
        }
    }
}
