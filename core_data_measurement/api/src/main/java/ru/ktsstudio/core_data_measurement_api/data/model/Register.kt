package ru.ktsstudio.core_data_measurement_api.data.model

/**
 * @author Maxim Ovchinnikov on 15.10.2020.
 */
data class Register(
    val containerTypes: List<ContainerType>,
    val wasteCategories: List<WasteCategory>,
    val measurementStatuses: List<MeasurementStatus>,
    val wasteGroups: List<WasteGroup>,
    val wasteSubgroups: List<WasteSubgroup>
)

data class ContainerType(
    val id: String,
    val name: String,
    val isSeparate: Boolean
)

data class WasteCategory(
    val id: String,
    val name: String
)

data class WasteGroup(
    val id: String,
    val name: String
)

data class WasteSubgroup(
    val id: String,
    val groupId: String,
    val name: String
)