package ru.ktsstudio.feature_mno_list.di.list

import dagger.Component
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.core_data_measurement_api.di.CoreMeasurementDataApi
import ru.ktsstudio.feature_mno_list.di.MnoCommonModule
import ru.ktsstudio.feature_mno_list.di.MnoNavigationApi
import ru.ktsstudio.feature_mno_list.ui.list.MnoListFragment

/**
 * @author Maxim Ovchinnikov on 01.10.2020.
 */
@Component(
    dependencies = [
        CoreApi::class,
        MnoNavigationApi::class,
        CoreMeasurementDataApi::class,
        MnoListDependencies::class
    ],
    modules = [MnoCommonModule::class, MnoListModule::class]
)
internal interface MnoListComponent {
    fun inject(fragment: MnoListFragment)
}