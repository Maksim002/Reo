package ru.ktsstudio.app_verification.di.object_survey.product

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
import ru.ktsstudio.app_verification.domain.object_survey.product.ProductDraftMerger
import ru.ktsstudio.app_verification.domain.object_survey.product.ProductDraftValidator
import ru.ktsstudio.app_verification.domain.object_survey.product.ProductEntityAdder
import ru.ktsstudio.app_verification.domain.object_survey.product.ProductEntityDeleter
import ru.ktsstudio.app_verification.domain.object_survey.product.ProductionDraftFactory
import ru.ktsstudio.app_verification.domain.object_survey.product.fill_validator.ProductFillValidator
import ru.ktsstudio.app_verification.domain.object_survey.product.fill_validator.ServiceFillValidator
import ru.ktsstudio.app_verification.domain.object_survey.product.models.ProductionSurveyDraft
import ru.ktsstudio.app_verification.presentation.exit.ExitSurveyViewModel
import ru.ktsstudio.app_verification.presentation.media.MediaSurveyViewModel
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.production.ProducedEntityType
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.production.ProductSurveyViewModel
import ru.ktsstudio.common.data.location.LocationRepository
import ru.ktsstudio.common.data.media.MediaRepository
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import ru.ktsstudio.core_data_verfication_api.data.model.production.Product
import ru.ktsstudio.core_data_verfication_api.data.model.production.Service
import javax.inject.Provider

@Module
internal interface ProductSurveyModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProductSurveyViewModel::class)
    fun bindViewModel(impl: ProductSurveyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MediaSurveyViewModel::class)
    fun bindMediaViewModel(impl: MediaSurveyViewModel<ProductionSurveyDraft>): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExitSurveyViewModel::class)
    fun bindExitSurveyViewModel(impl: ExitSurveyViewModel<ProductionSurveyDraft>): ViewModel

    @Binds
    fun bindProductFillValidator(impl: ProductFillValidator): FillValidator<Product>

    @Binds
    fun bindServiceFillValidator(impl: ServiceFillValidator): FillValidator<Service>

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
        fun provideSurveyFeature(
            objectRepository: ObjectRepository,
            locationRepository: LocationRepository,
            mediaRepository: MediaRepository,
            schedulers: SchedulerProvider,
            productFillValidator: FillValidator<Product>,
            serviceFillValidator: FillValidator<Service>
        ): Feature<
            ObjectSurveyFeature.Wish<ProductionSurveyDraft>,
            ObjectSurveyFeature.State<ProductionSurveyDraft>,
            ObjectSurveyFeature.News<ProductionSurveyDraft>
            > {
            return ObjectSurveyFeature(
                initialState = ObjectSurveyFeature.State(),
                actor = ObjectSurveyActor(
                    objectRepository = objectRepository,
                    locationRepository = locationRepository,
                    mediaRepository = mediaRepository,
                    schedulerProvider = schedulers,
                    draftFactory = ProductionDraftFactory(),
                    verificationObjectDraftMerger = ProductDraftMerger(),
                    draftValidator = ProductDraftValidator(
                        productFillValidator = productFillValidator,
                        serviceFillValidator = serviceFillValidator
                    )
                ),
                reducer = ObjectSurveyReducer(),
                newsPublisher = ObjectSurveyNewsPublisher()
            )
        }

        @Provides
        fun provideNestedObjectHolderFeature(): Feature<
            NestedObjectHolderFeature.Wish<ProductionSurveyDraft, ProducedEntityType>,
            NestedObjectHolderFeature.State<ProductionSurveyDraft>,
            NestedObjectHolderFeature.News> {
            return NestedObjectHolderFeature(
                initialState = NestedObjectHolderFeature.State(),
                actor = NestedObjectHolderActor(
                    adder = ProductEntityAdder(),
                    deleter = ProductEntityDeleter()
                ),
                reducer = NestedObjectHolderReducer()
            )
        }

        @Provides
        fun provideBinder(): Binder {
            return Binder()
        }
    }
}
