package ru.ktsstudio.app_verification.di.map.object_info

import dagger.BindsInstance
import dagger.Component
import ru.ktsstudio.app_verification.ui.map.ObjectInfoDialogFragment
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_data_verfication_api.di.CoreVerificationDataApi

@Component(
    dependencies = [CoreApi::class, CoreVerificationDataApi::class],
    modules = [ObjectInfoModule::class]
)
interface ObjectInfoComponent {
    fun inject(fragment: ObjectInfoDialogFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance @Id objectIds: List<String>,
            coreApi: CoreApi,
            coreDataApi: CoreVerificationDataApi
        ): ObjectInfoComponent
    }

    companion object {
        fun create(objectIds: List<String>): ObjectInfoComponent {
            return DaggerObjectInfoComponent.factory().create(
                objectIds = objectIds,
                coreApi = ComponentRegistry.get(),
                coreDataApi = ComponentRegistry.get()
            )
        }
    }
}
