package ru.ktsstudio.core_data_verfication_api.data.model.infrastructure

import arrow.core.MapK
import arrow.optics.optics
import com.google.gson.annotations.SerializedName
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

@optics
data class AvailabilityInfo(
    @SerializedName("isAvailable")
    val isAvailable: Boolean,
    @SerializedName("notAvailableReason")
    val notAvailableReason: String?
) {
    companion object
}

@optics
data class WeightControl(
    @SerializedName("availabilityInfo")
    val availabilityInfo: AvailabilityInfo,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("equipment")
    val equipment: MapK<String, WeightControlEquipment>
) {
    fun checkValidity(): Boolean = equipment.isEquipmentFormValid(
        availabilityInfo,
        itemCheck = { it.isFilled },
        itemCount = count
    )

    companion object
}

@optics
data class WeightControlEquipment(
    @SerializedName("id")
    val id: String,
    @SerializedName("remoteId")
    val remoteId: String?,
    @SerializedName("weightPlatformLength")
    val weightPlatformLength: Float?,
    @SerializedName("common")
    val commonEquipmentInfo: CommonEquipmentInfo
) {
    val isFilled: Boolean
        get() {
            return commonEquipmentInfo.checkFillWithoutCount() && weightPlatformLength != null
        }

    companion object {
        fun createEmpty(id: String): WeightControlEquipment {
            return WeightControlEquipment(
                id = id,
                remoteId = null,
                weightPlatformLength = null,
                commonEquipmentInfo = CommonEquipmentInfo.EMPTY
            )
        }
    }
}

@optics
data class WheelsWashing(
    @SerializedName("availabilityInfo")
    val availabilityInfo: AvailabilityInfo,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("equipment")
    val equipment: MapK<String, InfrastructureEquipment>
) {
    fun checkValidity(): Boolean = equipment.isEquipmentFormValid(
        availabilityInfo,
        itemCheck = { it.isFilled },
        itemCount = count
    )

    companion object
}

@optics
data class InfrastructureEquipment(
    @SerializedName("id")
    val id: String,
    @SerializedName("remoteId")
    val remoteId: String?,
    @SerializedName("common")
    val commonEquipmentInfo: CommonEquipmentInfo
) {
    val isFilled: Boolean
        get() = commonEquipmentInfo.checkFillWithoutCount()

    companion object {
        fun createEmpty(id: String): InfrastructureEquipment {
            return InfrastructureEquipment(
                id = id,
                remoteId = null,
                commonEquipmentInfo = CommonEquipmentInfo.EMPTY
            )
        }
    }
}

@optics
data class SewagePlant(
    @SerializedName("availabilityInfo")
    val availabilityInfo: AvailabilityInfo,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("equipment")
    val equipment: MapK<String, SewagePlantEquipment>
) {
    fun checkValidity(): Boolean = equipment.isEquipmentFormValid(
        availabilityInfo,
        itemCheck = { it.isFilled },
        itemCount = count
    )

    companion object
}

@optics
data class SewagePlantEquipment(
    @SerializedName("id")
    val id: String,
    @SerializedName("remoteId")
    val remoteId: String?,
    @SerializedName("power")
    val power: Float?,
    @SerializedName("sewageType")
    val type: Reference?,
    @SerializedName("photos")
    val photos: List<Media>,
    @SerializedName("passport")
    val passport: Media?
) {
    val isFilled: Boolean
        get() {
            return power != null &&
                type != null &&
                photos.isNotEmpty() &&
                passport != null
        }

    companion object {
        fun createEmpty(id: String): SewagePlantEquipment {
            return SewagePlantEquipment(
                id = id,
                remoteId = null,
                power = null,
                type = null,
                photos = emptyList(),
                passport = null
            )
        }
    }
}

@optics
data class RadiationControl(
    @SerializedName("availabilityInfo")
    val availabilityInfo: AvailabilityInfo,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("equipment")
    val equipment: MapK<String, InfrastructureEquipment>
) {
    fun checkValidity(): Boolean = equipment.isEquipmentFormValid(
        availabilityInfo,
        itemCheck = { it.isFilled },
        itemCount = count
    )

    companion object
}

@optics
data class SecurityCamera(
    @SerializedName("availabilityInfo")
    val availabilityInfo: AvailabilityInfo,
    @SerializedName("count")
    val count: Int?
) {
    fun checkValidity(): Boolean {
        return if (availabilityInfo.isAvailable) {
            count.isCountValid()
        } else {
            availabilityInfo.notAvailableReason.isNullOrBlank().not()
        }
    }

    companion object
}

@optics
data class RoadNetwork(
    @SerializedName("availabilityInfo")
    val availabilityInfo: AvailabilityInfo,
    @SerializedName("roadCoverageType")
    val roadCoverageType: Reference?,
    @SerializedName("roadLength")
    val roadLength: Float?,
    @SerializedName("schema")
    val schema: Media?
) {
    fun checkValidity(): Boolean {
        return if (availabilityInfo.isAvailable) {
            roadCoverageType != null &&
                roadLength != null &&
                schema != null
        } else {
            availabilityInfo.notAvailableReason.isNullOrBlank().not()
        }
    }

    companion object
}

@optics
data class Fences(
    @SerializedName("availabilityInfo")
    val availabilityInfo: AvailabilityInfo,
    @SerializedName("fenceType")
    val fenceType: Reference?,
    @SerializedName("fencePhotos")
    val fencePhotos: List<Media>
) {
    fun checkValidity(): Boolean {
        return if (availabilityInfo.isAvailable) {
            fenceType != null && fencePhotos.isNotEmpty()
        } else {
            availabilityInfo.notAvailableReason.isNullOrBlank().not()
        }
    }

    companion object
}

@optics
data class LightSystem(
    @SerializedName("availabilityInfo")
    val availabilityInfo: AvailabilityInfo,
    @SerializedName("lightSystemType")
    val lightSystemType: Reference?
) {
    fun checkValidity(): Boolean {
        return if (availabilityInfo.isAvailable) {
            lightSystemType != null
        } else {
            availabilityInfo.notAvailableReason.isNullOrBlank().not()
        }
    }

    companion object
}

@optics
data class SecurityStation(
    @SerializedName("availabilityInfo")
    val availabilityInfo: AvailabilityInfo,
    @SerializedName("securitySource")
    val securitySource: Reference?,
    @SerializedName("securityStaffCount")
    val securityStaffCount: Int?
) {
    fun checkValidity(): Boolean {
        return if (availabilityInfo.isAvailable) {
            securitySource != null && securityStaffCount.isCountValid()
        } else {
            availabilityInfo.notAvailableReason.isNullOrBlank().not()
        }
    }

    companion object
}

@optics
data class Asu(
    @SerializedName("availabilityInfo")
    val availabilityInfo: AvailabilityInfo,
    @SerializedName("systemName")
    val systemName: String?,
    @SerializedName("systemFunctions")
    val systemFunctions: String?
) {
    fun checkValidity(): Boolean {
        return if (availabilityInfo.isAvailable) {
            systemFunctions != null && systemName != null
        } else {
            availabilityInfo.notAvailableReason.isNullOrBlank().not()
        }
    }

    companion object
}

@optics
data class EnvironmentMonitoring(
    @SerializedName("availabilityInfo")
    val availabilityInfo: AvailabilityInfo,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("equipment")
    val equipment: MapK<String, EnvironmentMonitoringEquipment>
) {
    fun checkValidity(): Boolean = equipment.isEquipmentFormValid(
        availabilityInfo,
        itemCheck = { it.isFilled },
        itemCount = count
    )

    companion object
}

@optics
data class EnvironmentMonitoringEquipment(
    @SerializedName("id")
    val id: String,
    @SerializedName("systemTypeUsed")
    val systemTypeUsed: Reference?
) {
    val isFilled: Boolean
        get() = systemTypeUsed != null

    companion object {
        fun createEmpty(id: String): EnvironmentMonitoringEquipment {
            return EnvironmentMonitoringEquipment(
                id = id,
                systemTypeUsed = null
            )
        }
    }
}

fun <Equipment> MapK<String, Equipment>.isEquipmentFormValid(
    availabilityInfo: AvailabilityInfo,
    itemCheck: (Equipment) -> Boolean,
    itemCount: Int?
): Boolean {
    return if (availabilityInfo.isAvailable) {
        isNotEmpty() &&
            all { (_, equipment) -> itemCheck(equipment) } &&
            itemCount.isCountValid()
    } else {
        availabilityInfo.notAvailableReason.isNullOrBlank().not()
    }
}

fun CommonEquipmentInfo.checkFillWithoutCount(): Boolean {
    return with(this) {
        brand.isNotBlank() &&
            manufacturer.isNotBlank() &&
            commonMediaInfo.isFilled
    }
}

fun Int?.isCountValid(): Boolean {
    return this != null && this > 0
}