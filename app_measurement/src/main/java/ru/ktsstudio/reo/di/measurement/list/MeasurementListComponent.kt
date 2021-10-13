package ru.ktsstudio.reo.di.measurement.list

import dagger.Subcomponent
import ru.ktsstudio.reo.di.measurement.MeasurementCommonModule
import ru.ktsstudio.reo.di.measurement.filter.MeasurementFilterComponent
import ru.ktsstudio.reo.ui.measurement.list.MeasurementListFragment

/**
 * @author Maxim Ovchinnikov on 08.10.2020.
 */
@Subcomponent(
    modules = [MeasurementCommonModule::class, MeasurementListModule::class]
)
internal interface MeasurementListComponent {
    fun inject(fragment: MeasurementListFragment)
    fun filterComponent(): MeasurementFilterComponent
}
