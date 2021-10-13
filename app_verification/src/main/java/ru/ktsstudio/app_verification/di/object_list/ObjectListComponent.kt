package ru.ktsstudio.app_verification.di.object_list

import dagger.Component
import ru.ktsstudio.app_verification.di.app.VerificationAppComponent
import ru.ktsstudio.app_verification.di.object_list.dependencies.ObjectListDependencies
import ru.ktsstudio.app_verification.ui.object_list.ObjectListFragment
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_data_verfication_api.di.CoreVerificationDataApi

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.11.2020.
 */
@Component(
    dependencies = [
        CoreApi::class,
        ObjectNavigationApi::class,
        CoreVerificationDataApi::class,
        ObjectListDependencies::class
    ],
    modules = [ObjectListModule::class]
)
internal interface ObjectListComponent {

    fun inject(fragment: ObjectListFragment)

    companion object {
        fun create(): ObjectListComponent {
            return DaggerObjectListComponent.builder()
                .coreApi(ComponentRegistry.get())
                .objectNavigationApi(ComponentRegistry.get())
                .coreVerificationDataApi(ComponentRegistry.get())
                .objectListDependencies(
                    ComponentRegistry.get<VerificationAppComponent>()
                        .objectListDependencyComponent()
                )
                .build()
        }
    }
}
