package ru.ktsstudio.reo.di.create_measurement.create

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.reo.presentation.measurement.create_measurement.CreateMeasurementViewModel

/**
 * Created by Igor Park on 15/10/2020.
 */
@Module
interface CreateMeasurementModule {
    @Binds
    @IntoMap
    @ViewModelKey(CreateMeasurementViewModel::class)
    fun bindViewModel(impl: CreateMeasurementViewModel): ViewModel
}
