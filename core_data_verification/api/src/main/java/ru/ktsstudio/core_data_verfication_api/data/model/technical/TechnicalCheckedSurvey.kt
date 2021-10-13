package ru.ktsstudio.core_data_verfication_api.data.model.technical

import arrow.optics.optics

/**
 * @author Maxim Ovchinnikov on 13.12.2020.
 */
@optics
sealed class TechnicalCheckedSurvey {

    data class WasteTreatmentTechnicalCheckedSurvey(
        val tkoWeightForLastYear: Boolean = false,
        val otherWastesWeightForLastYear: Boolean = false,
        val objectArea: Boolean = false,
        val productionArea: Boolean = false,
        val wasteUnloadingArea: Boolean = false,
        val sortDepartmentArea: Boolean = false,
        val compostArea: Boolean = false,
        val rdfArea: Boolean = false,
        val schemePhotos: Boolean = false
    ) : TechnicalCheckedSurvey()

    data class WastePlacementTechnicalCheckedSurvey(
        val tkoWeightForLastYear: Boolean = false,
        val otherWastesWeightForLastYear: Boolean = false,
        val compostArea: Boolean = false,
        val rdfArea: Boolean = false,
        val objectArea: Boolean = false,
        val productionArea: Boolean = false,
        val objectBodyArea: Boolean = false,
        val polygonHeight: Boolean = false,
        val waterproofingType: Boolean = false,
        val relief: Boolean = false,
        val groundwaterDepth: Boolean = false,
        val wastePlacementMap: Boolean = false,
        val canFlooding: Boolean = false,
        val wasteSealant: Boolean = false,
        val schemePhotos: Boolean = false
    ) : TechnicalCheckedSurvey()

    @optics
    data class WasteRecyclingTechnicalCheckedSurvey(
        val recyclingType: Boolean = false,
        val techProcessComment: Boolean = false,
        val objectArea: Boolean = false,
        val productionArea: Boolean = false,
        val wasteUnloadingArea: Boolean = false,
        val temporaryWasteAccumulation: Boolean = false,
        val receivedWastes: Boolean = false,
        val mainEquipment: Boolean = false,
        val secondaryEquipment: Boolean = false,
        val receivedWastesWeightThisYear: Boolean = false,
        val receivedWastesWeightLastYear: Boolean = false,
        val schemePhotos: Boolean = false
    ) : TechnicalCheckedSurvey() {
        companion object
    }

    @optics
    data class WasteDisposalTechnicalCheckedSurvey(
        val objectArea: Boolean = false,
        val productionArea: Boolean = false,
        val techProcessComment: Boolean = false,
        val wasteUnloadingArea: Boolean = false,
        val mainEquipment: Boolean = false,
        val secondaryEquipment: Boolean = false,
        val thermalEnergyProduction: Boolean = false,
        val temporaryWasteAccumulation: Boolean = false,
        val receivedWastes: Boolean = false,
        val schemePhotos: Boolean = false
    ) : TechnicalCheckedSurvey() {
        companion object
    }

    companion object
}