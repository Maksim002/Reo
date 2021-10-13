package ru.ktsstudio.app_verification.di.object_survey.equipment

import dagger.BindsInstance
import dagger.Component
import ru.ktsstudio.app_verification.ui.object_survey.equipment.EquipmentSurveyFragment
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_data_verfication_api.di.CoreVerificationDataApi

/**
 * @author Maxim Ovchinnikov on 27.11.2020.
 */
@Component(
    dependencies = [CoreApi::class, CoreVerificationDataApi::class],
    modules = [EquipmentSurveyModule::class]
)
@FeatureScope
internal interface EquipmentSurveyComponent {
    fun inject(fragment: EquipmentSurveyFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance @Id objectId: String,
            coreApi: CoreApi,
            coreDataApi: CoreVerificationDataApi
        ): EquipmentSurveyComponent
    }

    companion object {
        fun create(objectId: String): EquipmentSurveyComponent {
            return DaggerEquipmentSurveyComponent.factory().create(
                objectId = objectId,
                coreApi = ComponentRegistry.get(),
                coreDataApi = ComponentRegistry.get()
            )
        }
    }
}
