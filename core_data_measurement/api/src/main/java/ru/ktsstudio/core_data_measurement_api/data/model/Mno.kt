package ru.ktsstudio.core_data_measurement_api.data.model
import ru.ktsstudio.common.data.models.GpsPoint

data class Mno(
    val objectInfo: ObjectInfo,
    val source: Source,
    val sourceAddress: SourceAddress,
    val containers: List<MnoContainer>
)

data class ObjectInfo(
    val taskIds: List<String>,
    val mnoId: String,
    val gpsPoint: GpsPoint
)

data class Source(
    val name: String,
    val type: String,
    val category: Category,
    val subcategory: String,
    val unit: Unit,
    val altUnit: Unit,
)
data class SourceAddress(
    val federalDistrict: String?,
    val region: String?,
    val municipalDistrict: String?,
    val address: String
)

data class Unit(
    val type: String,
    val quantity: Double
)

data class Category(
    val id: String,
    val name: String
)