package ru.ktsstudio.core_data_measurement_impl.data.network

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import ru.ktsstudio.common.data.models.RemoteSettings

/**
 * @author Maxim Ovchinnikov on 19.10.2020.
 */
interface SettingsNetworkApi {

    @GET("m/support")
    fun getSettings(): Single<RemoteSettings>
}