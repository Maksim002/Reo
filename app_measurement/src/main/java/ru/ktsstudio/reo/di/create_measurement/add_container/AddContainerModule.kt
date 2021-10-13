package ru.ktsstudio.reo.di.create_measurement.add_container

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.reo.presentation.measurement.add_container.AddContainerViewModel

/**
 * Created by Igor Park on 15/10/2020.
 */
@Module
interface AddContainerModule {
    @Binds
    @IntoMap
    @ViewModelKey(AddContainerViewModel::class)
    fun bindViewModel(impl: AddContainerViewModel): ViewModel
}
