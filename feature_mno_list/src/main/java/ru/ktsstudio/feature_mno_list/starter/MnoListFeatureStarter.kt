package ru.ktsstudio.feature_mno_list.starter

import androidx.fragment.app.Fragment
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_data_measurement_api.di.CoreMeasurementDataApi
import ru.ktsstudio.feature_mno_list.di.MnoNavigationApi
import ru.ktsstudio.feature_mno_list.di.list.DaggerMnoListComponent
import ru.ktsstudio.feature_mno_list.di.list.MnoListDependencies
import ru.ktsstudio.feature_mno_list.ui.list.MnoListFragment

/**
 * @author Maxim Myalkin (MaxMyalkin) on 14.10.2020.
 */
object MnoListFeatureStarter {
    fun start(
        coreApi: CoreApi,
        coreMeasurementDataApi: CoreMeasurementDataApi,
        mnoNavigationApi: MnoNavigationApi,
        mnoListDependencies: MnoListDependencies
    ): Fragment {
        DaggerMnoListComponent.builder()
            .coreApi(coreApi)
            .coreMeasurementDataApi(coreMeasurementDataApi)
            .mnoNavigationApi(mnoNavigationApi)
            .mnoListDependencies(mnoListDependencies)
            .build()
            .also { ComponentRegistry.register { it } }

        return MnoListFragment()
    }

}