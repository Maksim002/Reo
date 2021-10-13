package ru.ktsstudio.core_data_measurement_impl.data.network.model

import com.google.gson.annotations.SerializedName

/**
 * @author Maxim Ovchinnikov on 15.10.2020.
 */
data class RemoteRegister(
    @SerializedName("containerTypes") val containerTypes: List<ContainerType>,
    @SerializedName("wasteTypes") val wasteCategories: List<RemoteWasteCategory>,
    @SerializedName("measurementStatuses") val measurementStatuses: List<RemoteMeasurement.MeasurementStatus>,
    @SerializedName("wasteGroups") val wasteGroups: List<WasteGroup>,
    @SerializedName("wasteSubgroups") val wasteSubgroups: List<WasteSubgroup>
) {

    data class ContainerType(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("isSeparate") val isSeparate: Boolean
    )

    data class WasteGroup(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String
    )

    data class WasteSubgroup(
        @SerializedName("id") val id: String,
        @SerializedName("groupId") val groupId: String,
        @SerializedName("name") val name: String
    )
}