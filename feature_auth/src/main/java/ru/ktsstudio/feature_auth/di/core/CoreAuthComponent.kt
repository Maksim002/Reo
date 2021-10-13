package ru.ktsstudio.feature_auth.di.core

import dagger.Component
import ru.ktsstudio.common.data.account.LogoutCleaner
import ru.ktsstudio.common.data.db.DatabaseCleaner
import ru.ktsstudio.common.data.media.FileManager
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.di.CoreAuthApi
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_network_api.CoreNetworkApi

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.09.2020.
 */
@Component(
    modules = [CoreAuthModule::class],
    dependencies = [
        CoreApi::class,
        CoreNetworkApi::class,
        FileManager::class,
        DatabaseCleaner::class,
        LogoutCleaner::class
    ]
)
interface CoreAuthComponent : CoreAuthApi {
    companion object {
        fun create(
            fileManager: FileManager,
            databaseCleaner: DatabaseCleaner,
            logoutCleaner: LogoutCleaner
        ): CoreAuthComponent {
            return DaggerCoreAuthComponent.builder()
                .coreApi(ComponentRegistry.get())
                .coreNetworkApi(ComponentRegistry.get())
                .fileManager(fileManager)
                .databaseCleaner(databaseCleaner)
                .logoutCleaner(logoutCleaner)
                .build()
        }
    }
}