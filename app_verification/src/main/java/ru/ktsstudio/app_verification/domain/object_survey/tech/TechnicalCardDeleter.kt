package ru.ktsstudio.app_verification.domain.object_survey.tech

import arrow.core.MapK
import arrow.core.k
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectDeleter
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.TechnicalSurveyDraft
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.TechnicalCardType
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalSurvey

/**
 * @author Maxim Ovchinnikov on 28.12.2020.
 */
class TechnicalCardDeleter : NestedObjectDeleter<TechnicalSurveyDraft, TechnicalCardType> {

    override fun invoke(
        draft: TechnicalSurveyDraft,
        id: String,
        entityType: TechnicalCardType
    ): TechnicalSurveyDraft {
        return draft.deleteCard(id, entityType)
    }

    private fun TechnicalSurveyDraft.deleteCard(
        id: String,
        type: TechnicalCardType
    ): TechnicalSurveyDraft {
        return when (type) {
            TechnicalCardType.WASTE_PLACEMENT_MAP -> deletePlacementMap(id)
            TechnicalCardType.MAIN_EQUIPMENT -> deleteMainEquipment(id)
            TechnicalCardType.SECONDARY_EQUIPMENT -> deleteSecondaryEquipment(id)
        }
    }

    private fun TechnicalSurveyDraft.deletePlacementMap(id: String): TechnicalSurveyDraft {
        return copy(
            technicalSurvey = (technicalSurvey as TechnicalSurvey.WastePlacementTechnicalSurvey).copy(
                wastePlacementMaps = technicalSurvey.wastePlacementMaps.toMutableMap().apply {
                    remove(id)
                }
            )
        )
    }

    private fun TechnicalSurveyDraft.deleteMainEquipment(id: String): TechnicalSurveyDraft {
        return when (technicalSurvey) {
            is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> {
                copy(
                    technicalSurvey = technicalSurvey.copy(
                        mainEquipment = technicalSurvey.mainEquipment.deleteEquipment(id)
                    )
                )
            }
            is TechnicalSurvey.WasteDisposalTechnicalSurvey -> {
                copy(
                    technicalSurvey = technicalSurvey.copy(
                        mainEquipment = technicalSurvey.mainEquipment.deleteEquipment(id)
                    )
                )
            }
            else -> error("cannot delete main equipment from survey = $technicalSurvey")
        }
    }

    private fun TechnicalSurveyDraft.deleteSecondaryEquipment(id: String): TechnicalSurveyDraft {
        return when (technicalSurvey) {
            is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> {
                copy(
                    technicalSurvey = technicalSurvey.copy(
                        secondaryEquipment = technicalSurvey.secondaryEquipment.deleteEquipment(id)
                    )
                )
            }
            is TechnicalSurvey.WasteDisposalTechnicalSurvey -> {
                copy(
                    technicalSurvey = technicalSurvey.copy(
                        secondaryEquipment = technicalSurvey.secondaryEquipment.deleteEquipment(id)
                    )
                )
            }
            else -> error("cannot delete secondary equipment from survey = $technicalSurvey")
        }
    }

    private fun MapK<String, TechnicalEquipment>.deleteEquipment(id: String): MapK<String, TechnicalEquipment> {
        return toMutableMap()
            .apply { remove(id) }
            .k()
    }
}
