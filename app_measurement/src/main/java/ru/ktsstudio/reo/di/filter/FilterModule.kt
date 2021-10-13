package ru.ktsstudio.reo.di.filter

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
    @MnoFilter
    abstract fun bindsMnoFilterProvider(
        @MnoFilter impl: FilterStore
    ): FilterProvider

    @Binds
    @MnoFilter
    abstract fun bindsMnoFilterUpdater(
        @MnoFilter impl: FilterStore
    ): FilterUpdater

    @Binds
    @MeasurementFilter
    abstract fun bindsMeasurementFilterProvider(
        @MeasurementFilter impl: FilterStore
    ): FilterProvider

    @Binds
    @MeasurementFilter
    abstract fun bindsMeasurementFilterUpdater(
        @MeasurementFilter impl: FilterStore
    ): FilterUpdater

    companion object {
        @Provides
        @FeatureScope
        @MnoFilter
        fun providesMnoFilterStore(): FilterStore {
            return FilterStore()
        }

        @Provides
        @FeatureScope
        @MeasurementFilter
        fun providesMeasurementFilterStore(): FilterStore {
            return FilterStore()
        }

        @Provides
        fun providesLogoutCleaner(
            @MnoFilter mnoFilterUpdater: FilterUpdater,
            @MeasurementFilter measurementFilterUpdater: FilterUpdater
        ): LogoutCleaner {
            return object : LogoutCleaner {
                override fun cleanOnLogout(): Completable {
                    return Completable.fromCallable {
                        mnoFilterUpdater.updateFilter(Filter.EMPTY)
                        measurementFilterUpdater.updateFilter(Filter.EMPTY)
                    }
                }
            }
        }
    }
}
