package ru.ktsstudio.reo.di.create_measurement.edit_waste_type

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.reo.presentation.measurement.edit_waste_type.EditWasteTypeViewModel

/**
 * Created by Igor Park on 15/10/2020.
 */
@Module
interface EditWasteTypeModule {
    @Binds
    @IntoMap
    @ViewModelKey(EditWasteTypeViewModel::class)
    fun bindViewModel(impl: EditWasteTypeViewModel): ViewModel
}
