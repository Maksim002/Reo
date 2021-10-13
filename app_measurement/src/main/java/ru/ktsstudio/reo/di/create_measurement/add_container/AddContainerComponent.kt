package ru.ktsstudio.reo.di.create_measurement.add_container

import dagger.Subcomponent
import ru.ktsstudio.reo.ui.measurement.add_container.AddContainerFragment

/**
 * Created by Igor Park on 13/10/2020.
 */
@Subcomponent(
    modules = [AddContainerModule::class, AddContainerFeatureModule::class]
)
interface AddContainerComponent {
    fun inject(fragment: AddContainerFragment)
}
