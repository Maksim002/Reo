package ru.ktsstudio.reo.di.mno_list

import dagger.Subcomponent
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.domain.filter.FilterProvider
import ru.ktsstudio.common.domain.filter.FilterUpdater
import ru.ktsstudio.feature_mno_list.di.list.MnoListDependencies
import ru.ktsstudio.reo.di.filter.MnoFilter

/**
 * @author Maxim Myalkin (MaxMyalkin) on 14.10.2020.
 */

@Subcomponent
@FeatureScope
interface MnoListDependencyComponent {

    @MnoFilter
    fun filterProvider(): FilterProvider
    @MnoFilter
    fun filterUpdater(): FilterUpdater
}

fun MnoListDependencyComponent.toMnoListDependency(): MnoListDependencies {
    return object : MnoListDependencies {
        override fun filterProvider(): FilterProvider {
            return this@toMnoListDependency.filterProvider()
        }

        override fun filterUpdater(): FilterUpdater {
            return this@toMnoListDependency.filterUpdater()
        }
    }
}
