package ru.ktsstudio.reo.di.measurement.filter

import dagger.Subcomponent
import ru.ktsstudio.reo.ui.filter.MeasurementFilterFragment

/**
 * @author Maxim Myalkin (MaxMyalkin) on 20.10.2020.
 */
@Subcomponent(modules = [MeasurementFilterModule::class])
interface MeasurementFilterComponent {
    fun inject(fragment: MeasurementFilterFragment)
}
