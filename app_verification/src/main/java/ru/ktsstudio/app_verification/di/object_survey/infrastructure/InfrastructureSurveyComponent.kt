package ru.ktsstudio.app_verification.di.object_survey.infrastructure

import dagger.BindsInstance
import dagger.Component
import ru.ktsstudio.app_verification.ui.object_survey.infrastructure.InfrastructureSurveyFragment
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_data_verfication_api.di.CoreVerificationDataApi

@Component(
    dependencies = [CoreApi::class, CoreVerificationDataApi::class],
    modules = [InfrastructureSurveyModule::class]
)
@FeatureScope
internal interface InfrastructureSurveyComponent {
    fun inject(fragment: InfrastructureSurveyFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance @Id objectId: String,
            coreApi: CoreApi,
            coreDataApi: CoreVerificationDataApi
        ): InfrastructureSurveyComponent
    }

    companion object {
        fun create(objectId: String): InfrastructureSurveyComponent {
            return DaggerInfrastructureSurveyComponent.factory().create(
                objectId = objectId,
                coreApi = ComponentRegistry.get(),
                coreDataApi = ComponentRegistry.get()
            )
        }
    }
}
