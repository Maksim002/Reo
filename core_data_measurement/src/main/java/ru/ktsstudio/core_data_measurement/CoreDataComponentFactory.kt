package ru.ktsstudio.core_data_measurement

import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.core_data_measurement_api.di.CoreMeasurementDataApi
import ru.ktsstudio.core_data_measurement_impl.di.DataCoreMeasurementComponent
import ru.ktsstudio.core_network_api.CoreNetworkApi

object CoreDataComponentFactory {

    fun create(
        coreApi: CoreApi,
        coreNetworkApi: CoreNetworkApi,
        authority: String
    ): CoreMeasurementDataApi {
        return DataCoreMeasurementComponent.create(coreApi, coreNetworkApi, authority)
    }
}