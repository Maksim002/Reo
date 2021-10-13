package ru.ktsstudio.app_verification.di.object_survey.tech

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
import ru.ktsstudio.app_verification.domain.object_survey.tech.TechEquipmentFillValidator
import ru.ktsstudio.app_verification.domain.object_survey.tech.TechWastePlacementMapFillValidator
import ru.ktsstudio.app_verification.domain.object_survey.tech.TechnicalCardAdder
import ru.ktsstudio.app_verification.domain.object_survey.tech.TechnicalCardDeleter
import ru.ktsstudio.app_verification.domain.object_survey.tech.TechnicalDraftFactory
import ru.ktsstudio.app_verification.domain.object_survey.tech.TechnicalDraftMerger
import ru.ktsstudio.app_verification.domain.object_survey.tech.TechnicalDraftValidator
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.TechnicalSurveyDraft
import ru.ktsstudio.app_verification.presentation.exit.ExitSurveyViewModel
import ru.ktsstudio.app_verification.presentation.media.MediaSurveyViewModel
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.TechnicalCardType
import ru.ktsstudio.app_verification.presentation.object_survey.tech.ObjectTechnicalSurveyViewModel
import ru.ktsstudio.common.data.location.LocationRepository
import ru.ktsstudio.common.data.media.MediaRepository
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.technical.WastePlacementMap
import javax.inject.Provider

/**
 * @author Maxim Ovchinnikov on 02.12.2020.
 */
@Module
internal interface ObjectTechnicalSurveyModule {

    @Binds
    @IntoMap
    @ViewModelKey(ObjectTechnicalSurveyViewModel::class)
    fun bindViewModel(impl: ObjectTechnicalSurveyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MediaSurveyViewModel::class)
    fun bindMediaViewModel(impl: MediaSurveyViewModel<TechnicalSurveyDraft>): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExitSurveyViewModel::class)
    fun bindExitSurveyViewModel(impl: ExitSurveyViewModel<TechnicalSurveyDraft>): ViewModel

    @Binds
    fun bindTechEquipmentFillValidator(impl: TechEquipmentFillValidator): FillValidator<TechnicalEquipment>

    @Binds
    fun bindTechWastePlacementMapFillValidator(
        impl: TechWastePlacementMapFillValidator
    ): FillValidator<WastePlacementMap>

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
        fun provideObjectTechnicalFeature(
            objectRepository: ObjectRepository,
            locationRepository: LocationRepository,
            mediaRepository: MediaRepository,
            schedulers: SchedulerProvider,
            techEquipmentValidator: FillValidator<TechnicalEquipment>,
            techWastePlacementMapValidator: FillValidator<WastePlacementMap>
        ): Feature<
            ObjectSurveyFeature.Wish<TechnicalSurveyDraft>,
            ObjectSurveyFeature.State<TechnicalSurveyDraft>,
            ObjectSurveyFeature.News<TechnicalSurveyDraft>
            > {
            return ObjectSurveyFeature(
                initialState = ObjectSurveyFeature.State(),
                actor = ObjectSurveyActor(
                    objectRepository = objectRepository,
                    locationRepository = locationRepository,
                    mediaRepository = mediaRepository,
                    schedulerProvider = schedulers,
                    draftFactory = TechnicalDraftFactory(),
                    verificationObjectDraftMerger = TechnicalDraftMerger(),
                    draftValidator = TechnicalDraftValidator(techEquipmentValidator, techWastePlacementMapValidator)
                ),
                reducer = ObjectSurveyReducer(),
                newsPublisher = ObjectSurveyNewsPublisher()
            )
        }

        @Provides
        fun provideNestedObjectHolderFeature(): Feature<
            NestedObjectHolderFeature.Wish<TechnicalSurveyDraft, TechnicalCardType>,
            NestedObjectHolderFeature.State<TechnicalSurveyDraft>,
            NestedObjectHolderFeature.News> {
            return NestedObjectHolderFeature(
                initialState = NestedObjectHolderFeature.State(),
                actor = NestedObjectHolderActor(
                    adder = TechnicalCardAdder(),
                    deleter = TechnicalCardDeleter()
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
