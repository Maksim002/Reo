package ru.ktsstudio.core_data_verification_impl.data.network.model

import com.google.gson.annotations.SerializedName
import ru.ktsstudio.core_data_verfication_api.data.model.SurveyStatusType
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.11.2020.
 */
data class RemoteVerificationObject(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("latitude")
    val latitude: String?,
    @SerializedName("longitude")
    val longitude: String?,
    @SerializedName("wasteManagementType")
    val type: RemoteReference,
    @SerializedName("workSchedule")
    val workSchedule: SerializableObjectWorkSchedule,
    @SerializedName("addressDescription")
    val addressDescription: String,
    @SerializedName("technicalSurvey")
    val technicalSurvey: RemoteTechnicalSurvey?,
    @SerializedName("status")
    val objectStatus: RemoteReference?,
    @SerializedName("otherObjectStatusName")
    val otherObjectStatusName: String?,
    @SerializedName("infrastructureObjectInfo")
    val infrastructureSurvey: RemoteInfrastructureSurvey,
    @SerializedName("fiasAddress")
    val fiasAddress: RemoteFiasAddress?,
    @SerializedName("subjectName")
    val subject: RemoteReference,
    @SerializedName("verifiedFields")
    val verifiedFields: List<String>?,
    @SerializedName("indicator")
    val indicator: RemoteIndicator,
    @SerializedName("equipment")
    val equipment: List<RemoteEquipment>,
    @SerializedName("secondaryResources")
    val secondaryResources: RemoteSecondaryResources?,
    @SerializedName("productsInfo")
    val productionInfo: RemoteProductsInfo?,
    @SerializedName("hasCoreEquipment")
    val hasCoreEquipment: Boolean?,
    @SerializedName("hasAuxiliaryEquipment")
    val hasAuxiliaryEquipment: Boolean?,
    @SerializedName("wasteInfo")
    val wasteInfo: RemoteWasteInfo?
)

data class RemoteWasteInfo(
    @SerializedName("wasteTypes")
    val wasteTypes: List<RemoteWasteType>?,
    @SerializedName("vmrThisYear")
    val receivedWastesWeightThisYear: Float?,
    @SerializedName("vmrPreviousYear")
    val receivedWastesWeightLastYear: Float?
)

data class RemoteWasteType(
    @SerializedName("wasteType")
    val wasteType: RemoteReference
)

data class RemoteSurveyStatus(
    @SerializedName("displayName")
    val displayName: String?,
    @SerializedName("name")
    val statusType: SurveyStatusType
)

data class RemoteSecondaryResources(
    @SerializedName("extractPercent")
    val extractPercent: Float?,
    @SerializedName("types")
    val types: List<RemoteSecondaryResourceType>
)

data class RemoteIndicator(
    @SerializedName("previousYearTkoWeight")
    val tkoWeightForLastYear: Float?,
    @SerializedName("previousYearNonTkoWeight")
    val otherWastesWeightForLastYear: Float?,
    @SerializedName("area")
    val objectArea: Float?,
    @SerializedName("photos")
    val objectPhotos: List<SerializableMedia>?,
    @SerializedName("productionPartArea")
    val productionArea: Float?,
    @SerializedName("productionPartPhotos")
    val productionPhotos: List<SerializableMedia>?,
    @SerializedName("unloadingZoneArea")
    val wasteUnloadingArea: Float?,
    @SerializedName("sortingZoneArea")
    val sortDepartmentArea: Float?,
    @SerializedName("hasCompostingZone")
    val hasCompostArea: Boolean?,
    @SerializedName("noCompostingZoneReason")
    val noCompostAreaReason: String?,
    @SerializedName("compostingZoneCapacityPerYear")
    val compostAreaPower: Float?,
    @SerializedName("compostingMaterialName")
    val compostMaterial: String?,
    @SerializedName("compostPurpose")
    val compostPurpose: String?,
    @SerializedName("hasRdfZone")
    val hasRdfArea: Boolean?,
    @SerializedName("rdfZoneCapacityPerYear")
    val rdfPower: Float?,
    @SerializedName("rdfPurpose")
    val rdfPurpose: String?,
    @SerializedName("technologicalSchemePhotos")
    val techSchema: List<SerializableMedia>?,
    @SerializedName("generalSchemePhotos")
    val generalSchema: List<SerializableMedia>?,
    @SerializedName("productionZoneSchemePhotos")
    val productionSchema: List<SerializableMedia>?,
    @SerializedName("utilizationType")
    val recyclingType: RemoteReference?,
    @SerializedName("techProcessDescription")
    val techProcessComment: String?,
    @SerializedName("hasThermalRecycling")
    val hasThermalRecycling: Boolean?,
    @SerializedName("thermalRecyclingPower")
    val thermalRecyclingPower: Float?,
    @SerializedName("hasTemporaryWasteStorage")
    val hasTemporaryWasteAccumulation: Boolean?,
    @SerializedName("temporaryWasteStorageArea")
    val temporaryWasteArea: Float?,
    @SerializedName("temporaryWasteStorageWasteTypes")
    val temporaryWastes: String?,
    @SerializedName("placingArea")
    val objectBodyArea: Float?,
    @SerializedName("projectHeight")
    val polygonHeight: Float?,
    @SerializedName("waterproofingType")
    val waterproofingType: RemoteReference?,
    @SerializedName("relief")
    val relief: String?,
    @SerializedName("maps")
    val wastePlacementMaps: List<RemoteWastePlacementMap>?,
    @SerializedName("groundwaterDepth")
    val groundwaterDepth: Float?,
    @SerializedName("floodingPossibility")
    val canFlooding: Boolean?,
    @SerializedName("hasWasteCompactor")
    val hasWasteSealant: Boolean?,
    @SerializedName("wasteCompactorType")
    val sealantType: String?,
    @SerializedName("wasteCompactorMass")
    val sealantWeight: Float?,
    @SerializedName("wasteCompactorPhotos")
    val sealantPhotos: List<SerializableMedia>?
)

data class RemoteEquipment(
    @SerializedName("id")
    val id: String,
    @SerializedName("brand")
    val brand: String?,
    @SerializedName("manufacturer")
    val manufacturer: String?,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("projectPower")
    val power: Float?,
    @SerializedName("processDescription")
    val description: String?,
    @SerializedName("production")
    val production: String?,
    @SerializedName("photos")
    val photos: List<SerializableMedia>?,
    @SerializedName("passportPhotos")
    val passport: List<SerializableMedia>?,
    @SerializedName("length")
    val length: Float?,
    @SerializedName("width")
    val width: Float?,
    @SerializedName("speed")
    val speed: Float?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("type")
    val type: RemoteReference?,
    @SerializedName("kind")
    val kind: RemoteReference?,
    @SerializedName("loadingMechanism")
    val loadingMechanism: String?,
    @SerializedName("wasteWidth")
    val wasteHeight: Float?,
    @SerializedName("sortingPostsCount")
    val sortPointCount: Int?,
    @SerializedName("otherType")
    val otherEquipmentName: String?,
    @SerializedName("created")
    val created: Long?
)

data class RemoteWastePlacementMap(
    @SerializedName("id")
    val id: String,
    @SerializedName("period")
    val period: Long?,
    @SerializedName("size")
    val area: Float?
)

data class RemoteFiasAddress(
    @SerializedName("area")
    val area: String?,
    @SerializedName("buildingNumber")
    val buildingNumber: String?,
    @SerializedName("cadastralNumber")
    val cadastralNumber: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("id")
    val id: String,
    @SerializedName("innerCityTerritory")
    val innerCityTerritory: String?,
    @SerializedName("landPlotNumber")
    val landPlotNumber: String?,
    @SerializedName("municipalRegionName")
    val municipalRegionName: String?,
    @SerializedName("oktmo")
    val oktmo: String?,
    @SerializedName("planningStructureElement")
    val planningStructureElement: String?,
    @SerializedName("postalCode")
    val postalCode: String?,
    @SerializedName("regionName")
    val regionName: String?,
    @SerializedName("roomNumber")
    val roomNumber: String?,
    @SerializedName("streetRoadElement")
    val streetRoadElement: String?
)

data class RemoteSecondaryResourceType(
    @SerializedName("id")
    val id: String,
    @SerializedName("reference")
    val type: RemoteReference?,
    @SerializedName("percent")
    val percent: Float?,
    @SerializedName("otherSecondaryResourceTypeName")
    val otherName: String?
)

data class SerializableObjectWorkSchedule(
    @SerializedName("scheduleJson")
    val scheduleJson: String?,
    @SerializedName("shiftsPerDayCount")
    val shiftsPerDayCount: Int?,
    @SerializedName("daysPerYearCount")
    val daysPerYearCount: Int?,
    @SerializedName("workplacesCount")
    val workplacesCount: Int?,
    @SerializedName("managersCount")
    val managersCount: Int?,
    @SerializedName("workersCount")
    val workersCount: Int?
)

data class SerializableSchedule(
    @SerializedName("everyDay")
    val everyDay: Boolean,
    @SerializedName("fullDay")
    val fullDay: Boolean,
    @SerializedName("withoutBreak")
    val withoutBreak: Boolean,
    @SerializedName("days")
    val days: List<SerializableDay>?,
    @SerializedName("schedule")
    val workTime: SerializablePeriod?,
    @SerializedName("break")
    val breakTime: SerializablePeriod?
)

data class SerializableDay(
    @SerializedName("working")
    val isWorking: Boolean,
    @SerializedName("schedule")
    val workTime: SerializablePeriod?,
    @SerializedName("break")
    val breakTime: SerializablePeriod?
)

data class SerializablePeriod(
    @SerializedName("from")
    val from: String,
    @SerializedName("to")
    val to: String
)
