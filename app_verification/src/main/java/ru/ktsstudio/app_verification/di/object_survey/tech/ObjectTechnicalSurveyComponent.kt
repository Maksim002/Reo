package ru.ktsstudio.app_verification.di.object_survey.tech

import dagger.BindsInstance
import dagger.Component
import ru.ktsstudio.app_verification.ui.object_survey.tech.ObjectTechnicalSurveyFragment
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_data_verfication_api.di.CoreVerificationDataApi

/**
 * @author Maxim Ovchinnikov on 02.12.2020.
 */
@Component(
    dependencies = [CoreApi::class, CoreVerificationDataApi::class],
    modules = [ObjectTechnicalSurveyModule::class]
)
@FeatureScope
internal interface ObjectTechnicalSurveyComponent {
    fun inject(fragment: ObjectTechnicalSurveyFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance @Id objectId: String,
            coreApi: CoreApi,
            coreDataApi: CoreVerificationDataApi
        ): ObjectTechnicalSurveyComponent
    }

    companion object {
        fun create(objectId: String): ObjectTechnicalSurveyComponent {
            return DaggerObjectTechnicalSurveyComponent.factory().create(
                objectId = objectId,
                coreApi = ComponentRegistry.get(),
                coreDataApi = ComponentRegistry.get()
            )
        }
    }
}
