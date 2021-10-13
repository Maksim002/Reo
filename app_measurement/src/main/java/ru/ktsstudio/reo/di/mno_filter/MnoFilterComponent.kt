package ru.ktsstudio.reo.di.mno_filter

import dagger.Subcomponent
import ru.ktsstudio.reo.ui.filter.MnoFilterFragment

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
@Subcomponent(modules = [MnoFilterModule::class])
internal interface MnoFilterComponent {
    fun inject(fragment: MnoFilterFragment)
}
