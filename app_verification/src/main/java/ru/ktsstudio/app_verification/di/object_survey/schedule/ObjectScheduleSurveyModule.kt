package ru.ktsstudio.app_verification.di.object_survey.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyActor
import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyNewsPublisher
import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyReducer
import ru.ktsstudio.app_verification.domain.object_survey.schedule.ScheduleDraftFactory
import ru.ktsstudio.app_verification.domain.object_survey.schedule.ScheduleDraftMerger
import ru.ktsstudio.app_verification.domain.object_survey.schedule.ScheduleSurveyValidator
import ru.ktsstudio.app_verification.domain.object_survey.schedule.models.ScheduleSurveyDraft
import ru.ktsstudio.app_verification.presentation.exit.ExitSurveyViewModel
import ru.ktsstudio.app_verification.presentation.object_survey.schedule.ObjectScheduleSurveyViewModel
import ru.ktsstudio.common.data.location.LocationRepository
import ru.ktsstudio.common.data.media.MediaRepository
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import javax.inject.Provider

/**
 * @author Maxim Ovchinnikov on 27.11.2020.
 */
@Module
internal interface ObjectScheduleSurveyModule {

    @Binds
    @IntoMap
    @ViewModelKey(ObjectScheduleSurveyViewModel::class)
    fun bindViewModel(impl: ObjectScheduleSurveyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExitSurveyViewModel::class)
    fun bindExitSurveyViewModel(impl: ExitSurveyViewModel<ScheduleSurveyDraft>): ViewModel

    @Module
    companion object {

        @Provides
        fun provideViewModelProvider(
            viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
        ): ViewModelProvider.Factory {
            return ViewModelFactory(viewModelsMap)
        }

        @Provides
        @FeatureScope
        fun provideObjectScheduleFeature(
            objectRepository: ObjectRepository,
            locationRepository: LocationRepository,
            mediaRepository: MediaRepository,
            schedulers: SchedulerProvider
        ): Feature<
            ObjectSurveyFeature.Wish<ScheduleSurveyDraft>,
            ObjectSurveyFeature.State<ScheduleSurveyDraft>,
            ObjectSurveyFeature.News<ScheduleSurveyDraft>
            > {
            return ObjectSurveyFeature(
                initialState = ObjectSurveyFeature.State(),
                actor = ObjectSurveyActor(
                    objectRepository = objectRepository,
                    locationRepository = locationRepository,
                    mediaRepository = mediaRepository,
                    schedulerProvider = schedulers,
                    draftFactory = ScheduleDraftFactory(),
                    verificationObjectDraftMerger = ScheduleDraftMerger(),
                    draftValidator = ScheduleSurveyValidator()
                ),
                reducer = ObjectSurveyReducer(),
                newsPublisher = ObjectSurveyNewsPublisher()
            )
        }

        @Provides
        fun provideBinder(): Binder {
            return Binder()
        }
    }
}
