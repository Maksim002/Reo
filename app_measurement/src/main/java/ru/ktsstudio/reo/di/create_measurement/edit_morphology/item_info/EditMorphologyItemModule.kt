package ru.ktsstudio.reo.di.create_measurement.edit_morphology.item_info

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.reo.presentation.measurement.morphology.item_info.EditMorphologyItemViewModel

/**
 * Created by Igor Park on 15/10/2020.
 */
@Module
interface EditMorphologyItemModule {
    @Binds
    @IntoMap
    @ViewModelKey(EditMorphologyItemViewModel::class)
    fun bindViewModel(impl: EditMorphologyItemViewModel): ViewModel
}
