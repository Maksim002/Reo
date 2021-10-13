package ru.ktsstudio.app_verification.di.object_survey.general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.ktsstudio.app_verification.domain.object_survey.general.GeneralSurveyActor
import ru.ktsstudio.app_verification.domain.object_survey.general.GeneralSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.general.GeneralSurveyNewsPublisher
import ru.ktsstudio.app_verification.domain.object_survey.general.GeneralSurveyReducer
import ru.ktsstudio.app_verification.presentation.object_filter.VerificationObjectTypeUiMapper
import ru.ktsstudio.app_verification.presentation.object_survey.general.GeneralSurveyViewModel
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import javax.inject.Provider

@Module
internal interface GeneralSurveyModule {

    @Binds
    @IntoMap
    @ViewModelKey(GeneralSurveyViewModel::class)
    fun bindViewModel(impl: GeneralSurveyViewModel): ViewModel

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
        fun provideObjectScheduleFeature(
            objectRepository: ObjectRepository,
            schedulers: SchedulerProvider
        ): Feature<
            GeneralSurveyFeature.Wish,
            GeneralSurveyFeature.State,
            GeneralSurveyFeature.News
            > {
            return GeneralSurveyFeature(
                initialState = GeneralSurveyFeature.State(),
                actor = GeneralSurveyActor(objectRepository, schedulers),
                reducer = GeneralSurveyReducer(),
                newsPublisher = GeneralSurveyNewsPublisher()
            )
        }

        @Provides
        fun provideBinder(): Binder {
            return Binder()
        }
    }
}
