package ru.ktsstudio.core_data_verfication_api.data.model.technical

import arrow.optics.optics
import arrow.core.MapK
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

/**
 * @author Maxim Ovchinnikov on 13.12.2020.
 */
@optics
sealed class TechnicalSurvey {

    data class WasteTreatmentTechnicalSurvey(
        val tkoWeightForLastYear: Float?,
        val otherWastesWeightForLastYear: Float?,
        val objectArea: Float?,
        val objectPhotos: List<Media>,
        val productionArea: Float?,
        val productionPhotos: List<Media>,
        val wasteUnloadingArea: Float?,
        val sortDepartmentArea: Float?,
        val hasCompostArea: Boolean,
        val noCompostAreaReason: String?,
        val compostAreaPower: Float?,
        val compostMaterial: String?,
        val compostPurpose: String?,
        val hasRdfArea: Boolean,
        val rdfPower: Float?,
        val rdfPurpose: String?,
        val techSchema: List<Media>,
        val generalSchema: List<Media>,
        val productionSchema: List<Media>
    ) : TechnicalSurvey()

    data class WastePlacementTechnicalSurvey(
        val tkoWeightForLastYear: Float?,
        val otherWastesWeightForLastYear: Float?,
        val hasCompostArea: Boolean,
        val noCompostAreaReason: String?,
        val compostAreaPower: Float?,
        val compostMaterial: String?,
        val compostPurpose: String?,
        val hasRdfArea: Boolean,
        val rdfPower: Float?,
        val rdfPurpose: String?,
        val objectArea: Float?,
        val objectPhotos: List<Media>,
        val productionArea: Float?,
        val productionPhotos: List<Media>,
        val objectBodyArea: Float?,
        val polygonHeight: Float?,
        val waterproofingType: Reference?,
        val relief: String?,
        val groundWaterDepth: Float?,
        val wastePlacementMaps: Map<String, WastePlacementMap>,
        val canFlooding: Boolean,
        val hasWasteSealant: Boolean,
        val sealantType: String?,
        val sealantWeight: Float?,
        val sealantPhotos: List<Media>,
        val techSchema: List<Media>,
        val generalSchema: List<Media>,
        val productionSchema: List<Media>
    ) : TechnicalSurvey()

    @optics
    data class WasteRecyclingTechnicalSurvey(
        val recyclingType: Reference?,
        val techProcessComment: String?,
        val objectArea: Float?,
        val objectPhotos: List<Media>,
        val productionArea: Float?,
        val productionPhotos: List<Media>,
        val wasteUnloadingArea: Float?,
        val hasTemporaryWasteAccumulation: Boolean,
        val temporaryWasteArea: Float?,
        val temporaryWastes: String?,
        val techSchema: List<Media>,
        val generalSchema: List<Media>,
        val productionSchema: List<Media>,
        val hasMainEquipment: Boolean,
        val mainEquipment: MapK<String, TechnicalEquipment>,
        val hasSecondaryEquipment: Boolean,
        val secondaryEquipment: MapK<String, TechnicalEquipment>,
        val receivedWastes: List<Reference>,
        val receivedWastesWeightThisYear: Float?,
        val receivedWastesWeightLastYear: Float?
    ) : TechnicalSurvey() {
        companion object
    }

    @optics
    data class WasteDisposalTechnicalSurvey(
        val objectArea: Float?,
        val objectPhotos: List<Media>,
        val productionArea: Float?,
        val productionPhotos: List<Media>,
        val techProcessComment: String?,
        val wasteUnloadingArea: Float?,
        val hasThermalEnergyProduction: Boolean,
        val thermalEnergyProductionPower: Float?,
        val hasTemporaryWasteAccumulation: Boolean,
        val hasMainEquipment: Boolean,
        val mainEquipment: MapK<String, TechnicalEquipment>,
        val hasSecondaryEquipment: Boolean,
        val secondaryEquipment: MapK<String, TechnicalEquipment>,
        val temporaryWasteArea: Float?,
        val temporaryWastes: String?,
        val techSchema: List<Media>,
        val receivedWastes: List<Reference>,
        val generalSchema: List<Media>,
        val productionSchema: List<Media>
    ) : TechnicalSurvey() {
        companion object
    }

    companion object
}