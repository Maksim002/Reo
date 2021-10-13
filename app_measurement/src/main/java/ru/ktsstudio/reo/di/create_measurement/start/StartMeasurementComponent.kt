package ru.ktsstudio.reo.di.create_measurement.start

import dagger.Subcomponent
import ru.ktsstudio.reo.ui.measurement.start.StartMeasurementFragment

/**
 * Created by Igor Park on 13/10/2020.
 */
@Subcomponent(
    modules = [StartMeasurementModule::class, StartMeasurementFeatureModule::class]
)
interface StartMeasurementComponent {
    fun inject(fragment: StartMeasurementFragment)
}
