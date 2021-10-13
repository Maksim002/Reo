package ru.ktsstudio.app_verification.di.tabs

import dagger.Component
import ru.ktsstudio.app_verification.ui.tabs.TabsFragment
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common_registry.ComponentRegistry

/**
 * @author Maxim Ovchinnikov on 01.11.2020.
 */
@Component(
    dependencies = [CoreApi::class],
    modules = [TabsModule::class]
)
internal interface TabsComponent {

    fun inject(fragment: TabsFragment)

    companion object {
        fun create(): TabsComponent {
            return DaggerTabsComponent.builder()
                .coreApi(ComponentRegistry.get())
                .build()
        }
    }
}
