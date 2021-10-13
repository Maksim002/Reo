package ru.ktsstudio.reo.di.create_measurement.create

import dagger.BindsInstance
import dagger.Subcomponent
import ru.ktsstudio.common.di.qualifiers.MeasurementId
import ru.ktsstudio.reo.ui.measurement.create.CreateMeasurementFragment

/**
 * Created by Igor Park on 13/10/2020.
 */
@Subcomponent(
    modules = [CreateMeasurementModule::class, CreateMeasurementFeatureModule::class]
)
interface CreateMeasurementComponent {
    fun inject(fragment: CreateMeasurementFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance @MeasurementId measurementId: Long?): CreateMeasurementComponent
    }
}
