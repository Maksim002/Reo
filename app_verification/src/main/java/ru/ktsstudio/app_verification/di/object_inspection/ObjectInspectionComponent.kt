package ru.ktsstudio.app_verification.di.object_inspection

import dagger.BindsInstance
import dagger.Component
import ru.ktsstudio.app_verification.ui.object_inspection.ObjectInspectionFragment
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_data_verfication_api.di.CoreVerificationDataApi

/**
 * @author Maxim Ovchinnikov on 21.11.2020.
 */
@Component(
    dependencies = [CoreApi::class, CoreVerificationDataApi::class],
    modules = [ObjectInspectionModule::class]
)
internal interface ObjectInspectionComponent {
    fun inject(fragment: ObjectInspectionFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance @Id objectId: String,
            coreApi: CoreApi,
            coreDataApi: CoreVerificationDataApi
        ): ObjectInspectionComponent
    }

    companion object {
        fun create(objectId: String): ObjectInspectionComponent {
            return DaggerObjectInspectionComponent.factory().create(
                objectId = objectId,
                coreApi = ComponentRegistry.get(),
                coreDataApi = ComponentRegistry.get()
            )
        }
    }
}
