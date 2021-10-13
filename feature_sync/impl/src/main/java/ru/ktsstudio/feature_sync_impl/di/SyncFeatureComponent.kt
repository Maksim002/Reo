package ru.ktsstudio.feature_sync_impl.di

import dagger.BindsInstance
import dagger.Component
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.navigation.api.SyncNavigator
import ru.ktsstudio.feature_sync_api.di.CoreSyncDependency
import ru.ktsstudio.feature_sync_impl.di.fragment.SyncFragmentComponent
import ru.ktsstudio.feature_sync_impl.di.service.SyncServiceComponent

@Component(
    dependencies = [CoreApi::class, CoreSyncDependency::class]
)
interface SyncFeatureComponent : SyncFeatureApi {

    fun fragmentComponent(): SyncFragmentComponent
    fun serviceComponent(): SyncServiceComponent

    @Component.Factory
    interface Factory {

        fun create(
            coreApi: CoreApi,
            dependency: CoreSyncDependency,
            @BindsInstance syncNavigator: SyncNavigator
        ): SyncFeatureComponent
    }
}