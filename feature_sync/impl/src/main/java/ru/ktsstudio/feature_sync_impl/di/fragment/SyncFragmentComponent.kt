package ru.ktsstudio.feature_sync_impl.di.fragment

import dagger.Subcomponent
import ru.ktsstudio.feature_sync_impl.ui.SyncFragment

@Subcomponent(modules = [SyncFragmentModule::class])
interface SyncFragmentComponent {

    fun inject(fragment: SyncFragment)
}