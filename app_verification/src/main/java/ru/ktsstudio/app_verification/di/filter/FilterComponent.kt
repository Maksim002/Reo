package ru.ktsstudio.app_verification.di.filter

import dagger.Component
import ru.ktsstudio.common.data.account.LogoutCleaner
import ru.ktsstudio.common.di.FeatureScope

/**
 * @author Maxim Myalkin (MaxMyalkin) on 04.12.2020.
 */
@Component(
    modules = [FilterModule::class]
)
@FeatureScope
interface FilterComponent : FilterApi {
    fun logoutCleaner(): LogoutCleaner

    companion object {
        fun create(): FilterComponent {
            return DaggerFilterComponent.create()
        }
    }
}
