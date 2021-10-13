package ru.ktsstudio.app_verification.di.object_survey.product

import dagger.BindsInstance
import dagger.Component
import ru.ktsstudio.app_verification.ui.object_survey.product.ProductSurveyFragment
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_data_verfication_api.di.CoreVerificationDataApi

@Component(
    dependencies = [CoreApi::class, CoreVerificationDataApi::class],
    modules = [ProductSurveyModule::class]
)
@FeatureScope
internal interface ProductSurveyComponent {
    fun inject(fragment: ProductSurveyFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance @Id objectId: String,
            coreApi: CoreApi,
            coreDataApi: CoreVerificationDataApi
        ): ProductSurveyComponent
    }

    companion object {
        fun create(objectId: String): ProductSurveyComponent {
            return DaggerProductSurveyComponent.factory().create(
                objectId = objectId,
                coreApi = ComponentRegistry.get(),
                coreDataApi = ComponentRegistry.get()
            )
        }
    }
}
