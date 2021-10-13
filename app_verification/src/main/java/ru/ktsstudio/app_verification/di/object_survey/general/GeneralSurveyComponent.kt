package ru.ktsstudio.app_verification.di.object_survey.general

import dagger.BindsInstance
import dagger.Component
import ru.ktsstudio.app_verification.ui.object_survey.general.GeneralSurveyFragment
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_data_verfication_api.di.CoreVerificationDataApi

@Component(
    dependencies = [CoreApi::class, CoreVerificationDataApi::class],
    modules = [GeneralSurveyModule::class]
)
internal interface GeneralSurveyComponent {
    fun inject(fragment: GeneralSurveyFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance @Id objectId: String,
            coreApi: CoreApi,
            coreDataApi: CoreVerificationDataApi
        ): GeneralSurveyComponent
    }

    companion object {
        fun create(objectId: String): GeneralSurveyComponent {
            return DaggerGeneralSurveyComponent.factory().create(
                objectId = objectId,
                coreApi = ComponentRegistry.get(),
                coreDataApi = ComponentRegistry.get()
            )
        }
    }
}
