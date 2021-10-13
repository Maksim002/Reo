package ru.ktsstudio.app_verification.domain.object_survey.tech

import ru.ktsstudio.app_verification.domain.object_survey.common.SurveyDraftValidator
import ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator.FillValidator
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.TechnicalSurveyDraft
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.WastePlacementMap

/**
 * @author Maxim Ovchinnikov on 15.12.2020.
 */
class TechnicalDraftValidator(
    private val techEquipmentValidator: FillValidator<TechnicalEquipment>,
    private val techWastePlacementMapValidator: FillValidator<WastePlacementMap>
) : SurveyDraftValidator<TechnicalSurveyDraft> {
    override fun invoke(draft: TechnicalSurveyDraft): Boolean {
        return when (draft.technicalSurvey) {
            is TechnicalSurvey.WasteTreatmentTechnicalSurvey -> checkWasteTreatmentSurveyValidity(
                draft.technicalSurvey,
                draft.technicalCheckedSurvey as TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey
            )
            is TechnicalSurvey.WastePlacementTechnicalSurvey -> checkWastePlacementSurveyValidity(
                draft.technicalSurvey,
                draft.technicalCheckedSurvey as TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey
            )
            is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> checkWasteRecyclingSurveyValidity(
                draft.technicalSurvey,
                draft.technicalCheckedSurvey as TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey
            )
            is TechnicalSurvey.WasteDisposalTechnicalSurvey -> checkWasteDisposalSurveyValidity(
                draft.technicalSurvey,
                draft.technicalCheckedSurvey as TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey
            )
        }
    }

    private fun checkWasteTreatmentSurveyValidity(
        survey: TechnicalSurvey.WasteTreatmentTechnicalSurvey,
        checkedSurvey: TechnicalCheckedSurvey.WasteTreatmentTechnicalCheckedSurvey
    ): Boolean {
        return with(checkedSurvey) {
            checkEditItem(tkoWeightForLastYear, survey.tkoWeightForLastYear) &&
                checkEditItem(otherWastesWeightForLastYear, survey.otherWastesWeightForLastYear) &&
                checkEditItem(objectArea, survey.objectArea) &&
                checkEditItem(objectArea, null) { survey.objectPhotos.isNotEmpty() } &&
                checkEditItem(productionArea, survey.productionArea) &&
                checkEditItem(productionArea, null) { survey.productionPhotos.isNotEmpty() } &&
                checkEditItem(schemePhotos, null) {
                    survey.generalSchema.isNotEmpty() &&
                        survey.techSchema.isNotEmpty() &&
                        survey.productionSchema.isNotEmpty()
                } &&
                checkEditItem(wasteUnloadingArea, survey.wasteUnloadingArea) &&
                checkEditItem(sortDepartmentArea, survey.sortDepartmentArea) &&
                checkEditItem(wasteUnloadingArea, survey.wasteUnloadingArea) &&
                checkInnerSurveyItem(
                    isChecked = compostArea,
                    surveyAnswer = survey.hasCompostArea,
                    positiveData = listOf(
                        survey.compostMaterial,
                        survey.compostPurpose
                    ),
                    negativeData = listOf(
                        survey.noCompostAreaReason
                    )
                ) &&
                checkInnerSurveyItem(
                    isChecked = rdfArea,
                    surveyAnswer = survey.hasRdfArea,
                    positiveData = listOf(
                        survey.rdfPower,
                        survey.rdfPurpose
                    )
                )
        }
    }

    private fun checkWastePlacementSurveyValidity(
        survey: TechnicalSurvey.WastePlacementTechnicalSurvey,
        checkedSurvey: TechnicalCheckedSurvey.WastePlacementTechnicalCheckedSurvey
    ): Boolean {
        return with(checkedSurvey) {
            checkEditItem(tkoWeightForLastYear, survey.tkoWeightForLastYear) &&
                checkEditItem(otherWastesWeightForLastYear, survey.otherWastesWeightForLastYear) &&
                checkInnerSurveyItem(
                    isChecked = compostArea,
                    surveyAnswer = survey.hasCompostArea,
                    positiveData = listOf(
                        survey.compostMaterial,
                        survey.compostPurpose
                    ),
                    negativeData = listOf(
                        survey.noCompostAreaReason
                    )
                ) &&
                checkInnerSurveyItem(
                    isChecked = rdfArea,
                    surveyAnswer = survey.hasRdfArea,
                    positiveData = listOf(
                        survey.rdfPower,
                        survey.rdfPurpose
                    )
                ) &&
                checkEditItem(objectArea, survey.objectArea) &&
                checkEditItem(objectArea, null) { survey.objectPhotos.isNotEmpty() } &&
                checkEditItem(productionArea, survey.productionArea) &&
                checkEditItem(productionArea, null) { survey.productionPhotos.isNotEmpty() } &&
                checkEditItem(objectBodyArea, survey.objectBodyArea) &&
                checkEditItem(polygonHeight, survey.polygonHeight) &&
                checkEditItem(waterproofingType, survey.waterproofingType) &&
                checkEditItem(relief, survey.relief) &&
                checkEditItem(canFlooding, survey.canFlooding) &&
                checkEditItem(wasteSealant, survey.hasWasteSealant) &&
                checkEditItem(schemePhotos, null) {
                    survey.generalSchema.isNotEmpty() &&
                        survey.techSchema.isNotEmpty() &&
                        survey.productionSchema.isNotEmpty()
                } &&
                checkTechWastePlacementMap(wastePlacementMap, survey.wastePlacementMaps.values.toList())
        }
    }

    private fun checkWasteRecyclingSurveyValidity(
        survey: TechnicalSurvey.WasteRecyclingTechnicalSurvey,
        checkedSurvey: TechnicalCheckedSurvey.WasteRecyclingTechnicalCheckedSurvey
    ): Boolean {
        return with(checkedSurvey) {
            checkEditItem(recyclingType, survey.recyclingType) &&
                checkEditItem(techProcessComment, survey.techProcessComment) &&
                checkEditItem(objectArea, survey.objectArea) &&
                checkEditItem(objectArea, null) { survey.objectPhotos.isNotEmpty() } &&
                checkEditItem(productionArea, survey.productionArea) &&
                checkEditItem(productionArea, null) { survey.productionPhotos.isNotEmpty() } &&
                checkEditItem(wasteUnloadingArea, survey.wasteUnloadingArea) &&
                checkEditItem(temporaryWasteAccumulation, survey.hasTemporaryWasteAccumulation) &&
                checkEditItem(mainEquipment, survey.hasMainEquipment) &&
                checkEditItem(secondaryEquipment, survey.hasSecondaryEquipment) &&
                checkEditItem(receivedWastes, survey.receivedWastes) &&
                checkEditItem(receivedWastesWeightThisYear, survey.receivedWastesWeightThisYear) &&
                checkEditItem(receivedWastesWeightLastYear, survey.receivedWastesWeightLastYear) &&
                checkEditItem(schemePhotos, null) {
                    survey.generalSchema.isNotEmpty() &&
                        survey.techSchema.isNotEmpty() &&
                        survey.productionSchema.isNotEmpty()
                } &&
                checkTechEquipment(survey.hasMainEquipment, survey.mainEquipment.values.toList()) &&
                checkTechEquipment(survey.hasSecondaryEquipment, survey.secondaryEquipment.values.toList())
        }
    }

    private fun checkWasteDisposalSurveyValidity(
        survey: TechnicalSurvey.WasteDisposalTechnicalSurvey,
        checkedSurvey: TechnicalCheckedSurvey.WasteDisposalTechnicalCheckedSurvey
    ): Boolean {
        return with(checkedSurvey) {
            checkEditItem(objectArea, survey.objectArea) &&
                checkEditItem(objectArea, null) { survey.objectPhotos.isNotEmpty() } &&
                checkEditItem(productionArea, survey.productionArea) &&
                checkEditItem(productionArea, null) { survey.productionPhotos.isNotEmpty() } &&
                checkEditItem(techProcessComment, survey.techProcessComment) &&
                checkEditItem(wasteUnloadingArea, survey.wasteUnloadingArea) &&
                checkEditItem(thermalEnergyProduction, survey.hasThermalEnergyProduction) &&
                checkEditItem(mainEquipment, survey.hasMainEquipment) &&
                checkEditItem(secondaryEquipment, survey.hasSecondaryEquipment) &&
                checkEditItem(temporaryWasteAccumulation, survey.hasTemporaryWasteAccumulation) &&
                checkEditItem(receivedWastes, survey.receivedWastes) &&
                checkEditItem(schemePhotos, null) {
                    survey.generalSchema.isNotEmpty() &&
                        survey.techSchema.isNotEmpty() &&
                        survey.productionSchema.isNotEmpty()
                } &&
                checkTechEquipment(survey.hasMainEquipment, survey.mainEquipment.values.toList()) &&
                checkTechEquipment(survey.hasSecondaryEquipment, survey.secondaryEquipment.values.toList())
        }
    }

    private fun checkEditItem(
        isChecked: Boolean,
        data: Any?,
        validateFunc: Any?.() -> Boolean = { this.validate() }
    ): Boolean {
        return isChecked.not() || data.validateFunc()
    }

    private fun checkInnerSurveyItem(
        isChecked: Boolean,
        surveyAnswer: Boolean,
        positiveData: List<Any?> = emptyList(),
        negativeData: List<Any?> = emptyList()
    ): Boolean {
        val surveyValidated = when (surveyAnswer) {
            true -> positiveData.all { it.validate() }
            false -> negativeData.all { it.validate() }
        }
        return isChecked.not() || surveyValidated
    }

    private fun Any?.validate() = this?.toString().isNullOrBlank().not()

    private fun checkTechEquipment(hasEquipment: Boolean, equipment: List<TechnicalEquipment>): Boolean {
        return hasEquipment.not() || equipment.isNotEmpty() && equipment.all { techEquipmentValidator.isFilled(it) }
    }

    private fun checkTechWastePlacementMap(
        hasWastePlacementMap: Boolean,
        wastePlacementMaps: List<WastePlacementMap>
    ): Boolean {
        return hasWastePlacementMap.not() || wastePlacementMaps.isNotEmpty() && wastePlacementMaps.all {
            techWastePlacementMapValidator.isFilled(it)
        }
    }
}
