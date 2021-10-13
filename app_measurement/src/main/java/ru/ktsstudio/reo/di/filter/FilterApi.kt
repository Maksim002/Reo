package ru.ktsstudio.reo.di.filter

import ru.ktsstudio.common.domain.filter.FilterProvider
import ru.ktsstudio.common.domain.filter.FilterUpdater

/**
 * @author Maxim Myalkin (MaxMyalkin) on 16.11.2020.
 */
interface FilterApi {

    @MnoFilter
    fun mnoFilterProvider(): FilterProvider
    @MeasurementFilter
    fun measurementFilterProvider(): FilterProvider
    @MnoFilter
    fun mnoFilterUpdater(): FilterUpdater
    @MeasurementFilter
    fun measurementFilterUpdater(): FilterUpdater
}
