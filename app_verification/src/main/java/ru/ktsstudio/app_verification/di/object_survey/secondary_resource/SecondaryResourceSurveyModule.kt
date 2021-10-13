package ru.ktsstudio.app_verification.di.object_survey.secondary_resource

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
import ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator.FillValidator
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectHolderActor
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectHolderFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectHolderReducer
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceActor
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceReducer
import ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.SecondaryResourceDraftFactory
import ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.SecondaryResourceDraftMerger
import ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.SecondaryResourceTypeFillValidator
import ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.SecondaryResourcesAdder
import ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.SecondaryResourcesDeleter
import ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.SecondaryResourcesValidator
import ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.models.SecondaryResourcesSurveyDraft
import ru.ktsstudio.app_verification.presentation.exit.ExitSurveyViewModel
import ru.ktsstudio.app_verification.presentation.object_survey.secondary_resources.SecondaryResourceEntity
import ru.ktsstudio.app_verification.presentation.object_survey.secondary_resources.SecondaryResourcesSurveyViewModel
import ru.ktsstudio.common.data.location.LocationRepository
import ru.ktsstudio.common.data.media.MediaRepository
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.SecondaryResourceType
import javax.inject.Provider

/**
 * @author Maxim Ovchinnikov on 27.11.2020.
 */
@Module
internal interface SecondaryResourceSurveyModule {

    @Binds
    @IntoMap
    @ViewModelKey(SecondaryResourcesSurveyViewModel::class)
    fun bindViewModel(impl: SecondaryResourcesSurveyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExitSurveyViewModel::class)
    fun bindExitSurveyViewModel(impl: ExitSurveyViewModel<SecondaryResourcesSurveyDraft>): ViewModel

    @Binds
    fun bindSecondaryResourceTypeFillValidator(
        impl: SecondaryResourceTypeFillValidator
    ): FillValidator<SecondaryResourceType>

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
        fun provideEquipmentSurveyFeature(
            objectRepository: ObjectRepository,
            mediaRepository: MediaRepository,
            locationRepository: LocationRepository,
            schedulers: SchedulerProvider,
            validator: FillValidator<SecondaryResourceType>
        ): Feature<
            ObjectSurveyFeature.Wish<SecondaryResourcesSurveyDraft>,
            ObjectSurveyFeature.State<SecondaryResourcesSurveyDraft>,
            ObjectSurveyFeature.News<SecondaryResourcesSurveyDraft>
            > {
            return ObjectSurveyFeature(
                initialState = ObjectSurveyFeature.State(),
                actor = ObjectSurveyActor(
                    objectRepository = objectRepository,
                    schedulerProvider = schedulers,
                    draftFactory = SecondaryResourceDraftFactory(),
                    verificationObjectDraftMerger = SecondaryResourceDraftMerger(),
                    locationRepository = locationRepository,
                    mediaRepository = mediaRepository,
                    draftValidator = SecondaryResourcesValidator(validator)
                ),
                reducer = ObjectSurveyReducer(),
                newsPublisher = ObjectSurveyNewsPublisher()
            )
        }

        @Provides
        fun provideNestedObjectHolderFeature(): Feature<
            NestedObjectHolderFeature.Wish<SecondaryResourcesSurveyDraft, SecondaryResourceEntity>,
            NestedObjectHolderFeature.State<SecondaryResourcesSurveyDraft>,
            NestedObjectHolderFeature.News> {
            return NestedObjectHolderFeature(
                initialState = NestedObjectHolderFeature.State(),
                actor = NestedObjectHolderActor(
                    adder = SecondaryResourcesAdder(),
                    deleter = SecondaryResourcesDeleter()
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
