package ru.ktsstudio.app_verification.domain.object_survey.tech

import arrow.core.MapK
import arrow.core.k
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectAdder
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.TechnicalSurveyDraft
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.TechnicalCardType
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.WastePlacementMap
import java.util.UUID

/**
 * @author Maxim Ovchinnikov on 28.12.2020.
 */
class TechnicalCardAdder : NestedObjectAdder<TechnicalSurveyDraft, TechnicalCardType> {

    override fun invoke(
        draft: TechnicalSurveyDraft,
        cardType: TechnicalCardType
    ): TechnicalSurveyDraft {
        val id = generateId()
        return draft.addNewCard(id, cardType)
    }

    private fun generateId(): String = UUID.randomUUID().toString()

    private fun TechnicalSurveyDraft.addNewCard(
        id: String,
        type: TechnicalCardType
    ): TechnicalSurveyDraft {
        return when (type) {
            TechnicalCardType.WASTE_PLACEMENT_MAP -> addPlacementMap(id)
            TechnicalCardType.MAIN_EQUIPMENT -> addMainEquipment(id)
            TechnicalCardType.SECONDARY_EQUIPMENT -> addSecondaryEquipment(id)
        }
    }

    private fun TechnicalSurveyDraft.addPlacementMap(id: String): TechnicalSurveyDraft {
        return copy(
            technicalSurvey = (technicalSurvey as TechnicalSurvey.WastePlacementTechnicalSurvey).copy(
                wastePlacementMaps = technicalSurvey.wastePlacementMaps.toMutableMap().apply {
                    put(id, WastePlacementMap.createEmpty(id))
                }
            )
        )
    }

    private fun TechnicalSurveyDraft.addMainEquipment(id: String): TechnicalSurveyDraft {
        return when (technicalSurvey) {
            is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> {
                copy(
                    technicalSurvey = technicalSurvey.copy(
                        mainEquipment = technicalSurvey.mainEquipment.addEquipment(id)
                    )
                )
            }
            is TechnicalSurvey.WasteDisposalTechnicalSurvey -> {
                copy(
                    technicalSurvey = technicalSurvey.copy(
                        mainEquipment = technicalSurvey.mainEquipment.addEquipment(id)
                    )
                )
            }
            else -> error("cannot add main equipment to survey = $technicalSurvey")
        }
    }

    private fun TechnicalSurveyDraft.addSecondaryEquipment(id: String): TechnicalSurveyDraft {
        return when (technicalSurvey) {
            is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> {
                copy(
                    technicalSurvey = technicalSurvey.copy(
                        secondaryEquipment = technicalSurvey.secondaryEquipment.addEquipment(id)
                    )
                )
            }
            is TechnicalSurvey.WasteDisposalTechnicalSurvey -> {
                copy(
                    technicalSurvey = technicalSurvey.copy(
                        secondaryEquipment = technicalSurvey.secondaryEquipment.addEquipment(id)
                    )
                )
            }
            else -> error("cannot add secondary equipment to survey = $technicalSurvey")
        }
    }

    private fun MapK<String, TechnicalEquipment>.addEquipment(id: String): MapK<String, TechnicalEquipment> {
        return toMutableMap()
            .apply {
                put(id, TechnicalEquipment.createEmpty(id))
            }
            .k()
    }
}
