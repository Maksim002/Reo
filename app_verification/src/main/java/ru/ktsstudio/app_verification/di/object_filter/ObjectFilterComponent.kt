package ru.ktsstudio.app_verification.di.object_filter

import dagger.Subcomponent
import ru.ktsstudio.app_verification.ui.filter.ObjectFilterFragment

/**
 * @author Maxim Ovchinnikov on 18.11.2020.
 */
@Subcomponent(modules = [ObjectFilterModule::class])
internal interface ObjectFilterComponent {
    fun inject(fragment: ObjectFilterFragment)
}
