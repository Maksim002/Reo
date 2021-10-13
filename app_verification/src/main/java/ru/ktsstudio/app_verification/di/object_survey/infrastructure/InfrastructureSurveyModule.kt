package ru.ktsstudio.app_verification.di.object_survey.infrastructure

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
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectHolderActor
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectHolderFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectHolderReducer
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceActor
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceReducer
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.InfrastructureDraftValidator
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.InfrastructureEquipmentAdder
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.InfrastructureEquipmentDeleter
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.InfrastructureSurveyDraftFactory
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.InfrastructureSurveyDraftMerger
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.app_verification.presentation.exit.ExitSurveyViewModel
import ru.ktsstudio.app_verification.presentation.media.MediaSurveyViewModel
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.InfrastructureSurveyViewModel
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.InfrastructureEquipmentType
import ru.ktsstudio.common.data.location.LocationRepository
import ru.ktsstudio.common.data.media.MediaRepository
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import javax.inject.Provider

@Module
internal interface InfrastructureSurveyModule {

    @Binds
    @IntoMap
    @ViewModelKey(InfrastructureSurveyViewModel::class)
    fun bindViewModel(impl: InfrastructureSurveyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MediaSurveyViewModel::class)
    fun bindMediaViewModel(impl: MediaSurveyViewModel<InfrastructureSurveyDraft>): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExitSurveyViewModel::class)
    fun bindExitSurveyViewModel(impl: ExitSurveyViewModel<InfrastructureSurveyDraft>): ViewModel

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
            ObjectSurveyFeature.Wish<InfrastructureSurveyDraft>,
            ObjectSurveyFeature.State<InfrastructureSurveyDraft>,
            ObjectSurveyFeature.News<InfrastructureSurveyDraft>
            > {
            return ObjectSurveyFeature(
                initialState = ObjectSurveyFeature.State(),
                actor = ObjectSurveyActor(
                    objectRepository = objectRepository,
                    locationRepository = locationRepository,
                    mediaRepository = mediaRepository,
                    schedulerProvider = schedulers,
                    draftFactory = InfrastructureSurveyDraftFactory(),
                    verificationObjectDraftMerger = InfrastructureSurveyDraftMerger(),
                    draftValidator = InfrastructureDraftValidator()
                ),
                reducer = ObjectSurveyReducer(),
                newsPublisher = ObjectSurveyNewsPublisher()
            )
        }

        @Provides
        fun provideNestedObjectHolderFeature(): Feature<
            NestedObjectHolderFeature.Wish<InfrastructureSurveyDraft, InfrastructureEquipmentType>,
            NestedObjectHolderFeature.State<InfrastructureSurveyDraft>,
            NestedObjectHolderFeature.News> {
            return NestedObjectHolderFeature(
                initialState = NestedObjectHolderFeature.State(),
                actor = NestedObjectHolderActor(
                    adder = InfrastructureEquipmentAdder(),
                    deleter = InfrastructureEquipmentDeleter()
                ),
                reducer = NestedObjectHolderReducer()
            )
        }

        @Provides
        fun provideReferenceFeature(
            objectRepository: ObjectRepository,
            schedulers: SchedulerProvider
        ): Feature<
            ReferenceFeature.Wish,
            ReferenceFeature.State,
            Nothing
            > {
            return ReferenceFeature(
                initialState = ReferenceFeature.State(),
                actor = ReferenceActor(
                    objectRepository = objectRepository,
                    schedulers = schedulers
                ),
                reducer = ReferenceReducer()
            )
        }

        @Provides
        fun provideBinder(): Binder {
            return Binder()
        }
    }
}
