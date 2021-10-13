package ru.ktsstudio.reo.di.create_measurement.edit_morphology.section

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.reo.presentation.measurement.morphology.section.EditMorphologyViewModel

/**
 * Created by Igor Park on 15/10/2020.
 */
@Module
interface EditMorphologyModule {
    @Binds
    @IntoMap
    @ViewModelKey(EditMorphologyViewModel::class)
    fun bindViewModel(impl: EditMorphologyViewModel): ViewModel
}
