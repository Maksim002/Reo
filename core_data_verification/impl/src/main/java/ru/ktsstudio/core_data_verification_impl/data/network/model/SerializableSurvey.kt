package ru.ktsstudio.core_data_verification_impl.data.network.model

import com.google.gson.annotations.SerializedName
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ServingConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.technical.WastePlacementMap
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalEquipment

/**
 * @author Maxim Ovchinnikov on 26.11.2020.
 */

sealed class SerializableSurvey {
    abstract val type: VerificationObjectType

    data class SerializableWasteTreatmentSurvey(
        @SerializedName("technicalSurvey")
        val technicalSurvey: SerializableTechnicalSurvey,
        @SerializedName("infrastructureObjectInfos")
        val infrastructureSurvey: SerializableInfrastructureSurvey,
        @SerializedName("equipmentSurvey")
        val equipmentSurvey: SerializableEquipmentSurvey,
        @SerializedName("secondaryResourcesSurvey")
        val secondaryResourcesSurvey: SerializableSecondaryResourcesSurvey
    ) : SerializableSurvey() {

        @SerializedName("type")
        override val type: VerificationObjectType = VerificationObjectType.WASTE_TREATMENT

        data class SerializableTechnicalSurvey(
            @SerializedName("tkoWeightForLastYear")
            val tkoWeightForLastYear: Float?,
            @SerializedName("otherWastesWeightForLastYear")
            val otherWastesWeightForLastYear: Float?,
            @SerializedName("objectArea")
            val objectArea: Float?,
            @SerializedName("objectPhotos")
            val objectPhotos: List<SerializableMedia>,
            @SerializedName("productionArea")
            val productionArea: Float?,
            @SerializedName("productionPhotos")
            val productionPhotos: List<SerializableMedia>,
            @SerializedName("wasteUnloadingArea")
            val wasteUnloadingArea: Float?,
            @SerializedName("sortDepartmentArea")
            val sortDepartmentArea: Float?,
            @SerializedName("hasCompostArea")
            val hasCompostArea: Boolean,
            @SerializedName("noCompostAreaReason")
            val noCompostAreaReason: String?,
            @SerializedName("compostAreaPower")
            val compostAreaPower: Float?,
            @SerializedName("compostMaterial")
            val compostMaterial: String?,
            @SerializedName("compostPurpose")
            val compostPurpose: String?,
            @SerializedName("hasRdfArea")
            val hasRdfArea: Boolean,
            @SerializedName("rdfPower")
            val rdfPower: Float?,
            @SerializedName("rdfPurpose")
            val rdfPurpose: String?,
            @SerializedName("techSchema")
            val techSchema: List<SerializableMedia>,
            @SerializedName("generalSchema")
            val generalSchema: List<SerializableMedia>,
            @SerializedName("productionSchema")
            val productionSchema: List<SerializableMedia>
        )
    }

    data class SerializableWastePlacementSurvey(
        @SerializedName("technicalSurvey")
        val technicalSurvey: SerializableTechnicalSurvey,
        @SerializedName("infrastructureSurvey")
        val infrastructureSurvey: SerializableInfrastructureSurvey
    ) : SerializableSurvey() {

        @SerializedName("type")
        override val type: VerificationObjectType = VerificationObjectType.WASTE_PLACEMENT

        data class SerializableTechnicalSurvey(
            @SerializedName("tkoWeightForLastYear")
            val tkoWeightForLastYear: Float?,
            @SerializedName("otherWastesWeightForLastYear")
            val otherWastesWeightForLastYear: Float?,
            @SerializedName("hasCompostArea")
            val hasCompostArea: Boolean,
            @SerializedName("noCompostAreaReason")
            val noCompostAreaReason: String?,
            @SerializedName("compostAreaPower")
            val compostAreaPower: Float?,
            @SerializedName("compostMaterial")
            val compostMaterial: String?,
            @SerializedName("compostPurpose")
            val compostPurpose: String?,
            @SerializedName("hasRdfArea")
            val hasRdfArea: Boolean,
            @SerializedName("rdfPower")
            val rdfPower: Float?,
            @SerializedName("rdfPurpose")
            val rdfPurpose: String?,
            @SerializedName("objectArea")
            val objectArea: Float?,
            @SerializedName("objectPhotos")
            val objectPhotos: List<SerializableMedia>,
            @SerializedName("productionArea")
            val productionArea: Float?,
            @SerializedName("productionPhotos")
            val productionPhotos: List<SerializableMedia>,
            @SerializedName("objectBodyArea")
            val objectBodyArea: Float?,
            @SerializedName("polygonHeight")
            val polygonHeight: Float?,
            @SerializedName("waterproofingType")
            val waterproofingType: Reference?,
            @SerializedName("relief")
            val relief: String?,
            @SerializedName("groundwaterDepth")
            val groundwaterDepth: Float?,
            @SerializedName("wastePlacementMap")
            val wastePlacementMap: List<WastePlacementMap>,
            @SerializedName("canFlooding")
            val canFlooding: Boolean,
            @SerializedName("hasWasteSealant")
            val hasWasteSealant: Boolean,
            @SerializedName("sealantType")
            val sealantType: String?,
            @SerializedName("sealantWeight")
            val sealantWeight: Float?,
            @SerializedName("sealantPhotos")
            val sealantPhotos: List<SerializableMedia>,
            @SerializedName("techSchema")
            val techSchema: List<SerializableMedia>,
            @SerializedName("generalSchema")
            val generalSchema: List<SerializableMedia>,
            @SerializedName("productionSchema")
            val productionSchema: List<SerializableMedia>
        )
    }

    data class SerializableWasteRecyclingSurvey(
        @SerializedName("technicalSurvey")
        val technicalSurvey: SerializableTechnicalSurvey,
        @SerializedName("infrastructureSurvey")
        val infrastructureSurvey: SerializableInfrastructureSurvey,
        @SerializedName("productionSurvey")
        val productionSurvey: SerializableProductionSurvey
    ) : SerializableSurvey() {

        @SerializedName("type")
        override val type: VerificationObjectType = VerificationObjectType.WASTE_RECYCLING

        data class SerializableTechnicalSurvey(
            @SerializedName("recyclingType")
            val recyclingType: Reference?,
            @SerializedName("techProcessComment")
            val techProcessComment: String?,
            @SerializedName("objectArea")
            val objectArea: Float?,
            @SerializedName("objectPhotos")
            val objectPhotos: List<SerializableMedia>,
            @SerializedName("productionArea")
            val productionArea: Float?,
            @SerializedName("productionPhotos")
            val productionPhotos: List<SerializableMedia>,
            @SerializedName("wasteUnloadingArea")
            val wasteUnloadingArea: Float?,
            @SerializedName("hasTemporaryWasteAccumulation")
            val hasTemporaryWasteAccumulation: Boolean,
            @SerializedName("temporaryWasteArea")
            val temporaryWasteArea: Float?,
            @SerializedName("temporaryWastes")
            val temporaryWastes: String?,
            @SerializedName("techSchema")
            val techSchema: List<SerializableMedia>,
            @SerializedName("generalSchema")
            val generalSchema: List<SerializableMedia>,
            @SerializedName("productionSchema")
            val productionSchema: List<SerializableMedia>,
            @SerializedName("receivedWastes")
            val receivedWastes: List<Reference>?,
            @SerializedName("receivedWastesWeightThisYear")
            val receivedWastesWeightThisYear: Float?,
            @SerializedName("receivedWastesWeightLastYear")
            val receivedWastesWeightLastYear: Float?,
            @SerializedName("hasMainEquipment")
            val hasMainEquipment: Boolean,
            @SerializedName("mainEquipment")
            val mainEquipment: List<TechnicalEquipment>,
            @SerializedName("hasSecondaryEquipment")
            val hasSecondaryEquipment: Boolean,
            @SerializedName("secondaryEquipment")
            val secondaryEquipment: List<TechnicalEquipment>
        )
    }

    data class SerializableWasteDisposalSurvey(
        @SerializedName("technicalSurvey")
        val technicalSurvey: SerializableTechnicalSurvey,
        @SerializedName("infrastructureSurvey")
        val infrastructureSurvey: SerializableInfrastructureSurvey,
        @SerializedName("productionSurvey")
        val productionSurvey: SerializableProductionSurvey
    ) : SerializableSurvey() {

        @SerializedName("type")
        override val type: VerificationObjectType = VerificationObjectType.WASTE_DISPOSAL

        data class SerializableTechnicalSurvey(
            @SerializedName("objectArea")
            val objectArea: Float?,
            @SerializedName("objectPhotos")
            val objectPhotos: List<SerializableMedia>,
            @SerializedName("productionArea")
            val productionArea: Float?,
            @SerializedName("productionPhotos")
            val productionPhotos: List<SerializableMedia>,
            @SerializedName("techProcessComment")
            val techProcessComment: String?,
            @SerializedName("wasteUnloadingArea")
            val wasteUnloadingArea: Float?,
            @SerializedName("hasThermalEnergyProduction")
            val hasThermalEnergyProduction: Boolean,
            @SerializedName("thermalEnergyProductionPower")
            val thermalEnergyProductionPower: Float?,
            @SerializedName("hasTemporaryWasteAccumulation")
            val hasTemporaryWasteAccumulation: Boolean,
            @SerializedName("temporaryWasteArea")
            val temporaryWasteArea: Float?,
            @SerializedName("temporaryWastes")
            val temporaryWastes: String?,
            @SerializedName("techSchema")
            val techSchema: List<SerializableMedia>,
            @SerializedName("generalSchema")
            val generalSchema: List<SerializableMedia>,
            @SerializedName("productionSchema")
            val productionSchema: List<SerializableMedia>,
            @SerializedName("receivedWastes")
            val receivedWastes: List<Reference>?,
            @SerializedName("hasMainEquipment")
            val hasMainEquipment: Boolean,
            @SerializedName("mainEquipment")
            val mainEquipment: List<TechnicalEquipment>,
            @SerializedName("hasSecondaryEquipment")
            val hasSecondaryEquipment: Boolean,
            @SerializedName("secondaryEquipment")
            val secondaryEquipment: List<TechnicalEquipment>,
        )
    }
}