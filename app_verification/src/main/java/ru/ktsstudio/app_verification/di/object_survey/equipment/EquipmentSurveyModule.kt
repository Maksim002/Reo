package ru.ktsstudio.app_verification.di.object_survey.equipment

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
import ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator.CommonEquipmentInfoFillValidator
import ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator.FillValidator
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectHolderActor
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectHolderFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectHolderReducer
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceActor
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceReducer
import ru.ktsstudio.app_verification.domain.object_survey.equipment.EquipmentAdder
import ru.ktsstudio.app_verification.domain.object_survey.equipment.EquipmentDeleter
import ru.ktsstudio.app_verification.domain.object_survey.equipment.EquipmentDraftFactory
import ru.ktsstudio.app_verification.domain.object_survey.equipment.EquipmentDraftMerger
import ru.ktsstudio.app_verification.domain.object_survey.equipment.fill_validator.AdditionalEquipmentFillValidator
import ru.ktsstudio.app_verification.domain.object_survey.equipment.fill_validator.BagBreakerFillValidator
import ru.ktsstudio.app_verification.domain.object_survey.equipment.fill_validator.CommonConveyorInfoFillValidator
import ru.ktsstudio.app_verification.domain.object_survey.equipment.fill_validator.ConveyorFillValidator
import ru.ktsstudio.app_verification.domain.object_survey.equipment.fill_validator.EquipmentSurveyDraftValidator
import ru.ktsstudio.app_verification.domain.object_survey.equipment.fill_validator.PressFillValidator
import ru.ktsstudio.app_verification.domain.object_survey.equipment.fill_validator.SeparatorFillValidator
import ru.ktsstudio.app_verification.domain.object_survey.equipment.fill_validator.ServingConveyorFillValidator
import ru.ktsstudio.app_verification.domain.object_survey.equipment.fill_validator.SortConveyorFillValidator
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.EquipmentSurveyDraft
import ru.ktsstudio.app_verification.presentation.exit.ExitSurveyViewModel
import ru.ktsstudio.app_verification.presentation.media.MediaSurveyViewModel
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.EquipmentEntity
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.EquipmentSurveyViewModel
import ru.ktsstudio.common.data.location.LocationRepository
import ru.ktsstudio.common.data.media.MediaRepository
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.AdditionalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.BagBreakerConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonConveyorInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Conveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Press
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Separator
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ServingConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SortConveyor
import javax.inject.Provider

/**
 * @author Maxim Ovchinnikov on 27.11.2020.
 */
@Module
internal interface EquipmentSurveyModule {

    @Binds
    @IntoMap
    @ViewModelKey(EquipmentSurveyViewModel::class)
    fun bindViewModel(impl: EquipmentSurveyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MediaSurveyViewModel::class)
    fun bindMediaViewModel(impl: MediaSurveyViewModel<EquipmentSurveyDraft>): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExitSurveyViewModel::class)
    fun bindExitSurveyViewModel(impl: ExitSurveyViewModel<EquipmentSurveyDraft>): ViewModel

    @Binds
    fun bindCommonEquipmentInfoFillValidator(impl: CommonEquipmentInfoFillValidator): FillValidator<CommonEquipmentInfo>

    @Binds
    fun bindCommonConveyorInfoFillValidator(impl: CommonConveyorInfoFillValidator): FillValidator<CommonConveyorInfo>

    @Binds
    fun bindServingConveyorFillValidator(impl: ServingConveyorFillValidator): FillValidator<ServingConveyor>

    @Binds
    fun bindSortConveyorFillValidator(impl: SortConveyorFillValidator): FillValidator<SortConveyor>

    @Binds
    fun bindConveyorFillValidator(impl: ConveyorFillValidator): FillValidator<Conveyor>

    @Binds
    fun bindBagBreakerFillValidator(impl: BagBreakerFillValidator): FillValidator<BagBreakerConveyor>

    @Binds
    fun bindPressFillValidator(impl: PressFillValidator): FillValidator<Press>

    @Binds
    fun bindSeparatorFillValidator(impl: SeparatorFillValidator): FillValidator<Separator>

    @Binds
    fun bindAdditionalEquipmentFillValidator(impl: AdditionalEquipmentFillValidator): FillValidator<AdditionalEquipment>

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
            locationRepository: LocationRepository,
            mediaRepository: MediaRepository,
            schedulers: SchedulerProvider,
            servingConveyorFillValidator: FillValidator<ServingConveyor>,
            sortConveyorFillValidator: FillValidator<SortConveyor>,
            conveyorFillValidator: FillValidator<Conveyor>,
            bagBreakerFillValidator: FillValidator<BagBreakerConveyor>,
            separatorFillValidator: FillValidator<Separator>,
            pressFillValidator: FillValidator<Press>,
            additionalFillValidator: FillValidator<AdditionalEquipment>
        ): Feature<
            ObjectSurveyFeature.Wish<EquipmentSurveyDraft>,
            ObjectSurveyFeature.State<EquipmentSurveyDraft>,
            ObjectSurveyFeature.News<EquipmentSurveyDraft>
            > {
            return ObjectSurveyFeature(
                initialState = ObjectSurveyFeature.State(),
                actor = ObjectSurveyActor(
                    objectRepository = objectRepository,
                    locationRepository = locationRepository,
                    mediaRepository = mediaRepository,
                    schedulerProvider = schedulers,
                    draftFactory = EquipmentDraftFactory(),
                    verificationObjectDraftMerger = EquipmentDraftMerger(),
                    draftValidator = EquipmentSurveyDraftValidator(
                        servingConveyorFillValidator,
                        sortConveyorFillValidator,
                        conveyorFillValidator,
                        bagBreakerFillValidator,
                        separatorFillValidator,
                        pressFillValidator,
                        additionalFillValidator
                    )
                ),
                reducer = ObjectSurveyReducer(),
                newsPublisher = ObjectSurveyNewsPublisher()
            )
        }

        @Provides
        fun provideNestedObjectHolderFeature(): Feature<
            NestedObjectHolderFeature.Wish<EquipmentSurveyDraft, EquipmentEntity>,
            NestedObjectHolderFeature.State<EquipmentSurveyDraft>,
            NestedObjectHolderFeature.News> {
            return NestedObjectHolderFeature(
                initialState = NestedObjectHolderFeature.State(),
                actor = NestedObjectHolderActor(
                    adder = EquipmentAdder(),
                    deleter = EquipmentDeleter()
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
