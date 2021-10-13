package ru.ktsstudio.reo.di.create_measurement.edit_separate_container

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.reo.presentation.measurement.edit_separate_container.EditSeparateContainerViewModel

/**
 * Created by Igor Park on 15/10/2020.
 */
@Module
interface EditSeparateContainerModule {
    @Binds
    @IntoMap
    @ViewModelKey(EditSeparateContainerViewModel::class)
    fun bindViewModel(impl: EditSeparateContainerViewModel): ViewModel
}
