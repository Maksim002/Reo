package ru.ktsstudio.core_data_measurement_api.data.model

data class MnoContainer(
    val id: String,
    val name: String,
    val type: ContainerType,
    val volume: Float,
    val scheduleType: String
)