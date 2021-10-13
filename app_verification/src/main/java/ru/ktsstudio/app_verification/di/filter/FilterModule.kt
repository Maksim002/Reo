package ru.ktsstudio.app_verification.di.filter

import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Completable
import ru.ktsstudio.common.data.account.LogoutCleaner
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.domain.filter.Filter
import ru.ktsstudio.common.domain.filter.FilterProvider
import ru.ktsstudio.common.domain.filter.FilterStore
import ru.ktsstudio.common.domain.filter.FilterUpdater

/**
 * @author Maxim Myalkin (MaxMyalkin) on 14.10.2020.
 */
@Module
internal abstract class FilterModule {
    @Binds
    abstract fun bindsFilterProvider(impl: FilterStore): FilterProvider

    @Binds
    abstract fun bindsFilterUpdater(impl: FilterStore): FilterUpdater

    companion object {
        @Provides
        @FeatureScope
        fun providesFilterStore(): FilterStore {
            return FilterStore()
        }

        @Provides
        fun providesLogoutCleaner(
            objectFilterUpdater: FilterUpdater,
        ): LogoutCleaner {
            return object : LogoutCleaner {
                override fun cleanOnLogout(): Completable {
                    return Completable.fromCallable {
                        objectFilterUpdater.updateFilter(Filter.EMPTY)
                    }
                }
            }
        }
    }
}
