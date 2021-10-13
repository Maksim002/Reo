package ru.ktsstudio.app_verification.domain.object_survey.infrastructure

import arrow.core.k
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectDeleter
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.infrastructureSurvey
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.InfrastructureEquipmentType
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.environmentMonitoring
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.equipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.radiationControl
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.sewagePlant
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.weightControl
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.wheelsWashing

class InfrastructureEquipmentDeleter : NestedObjectDeleter<
    InfrastructureSurveyDraft,
    InfrastructureEquipmentType
    > {

    override fun invoke(
        draft: InfrastructureSurveyDraft,
        id: String,
        entityType: InfrastructureEquipmentType
    ): InfrastructureSurveyDraft {
        return draft.deleteEquipment(id, entityType)
    }

    private fun InfrastructureSurveyDraft.deleteEquipment(
        id: String,
        type: InfrastructureEquipmentType
    ): InfrastructureSurveyDraft {
        val infrastructureSurveyOptics = InfrastructureSurveyDraft.infrastructureSurvey
        return when (type) {
            InfrastructureEquipmentType.WHEELS_WASHING -> {
                infrastructureSurveyOptics.wheelsWashing.equipment.modify(this) {
                    it.minus(id).k()
                }
            }
            InfrastructureEquipmentType.WEIGHT_CONTROL -> {
                infrastructureSurveyOptics.weightControl.equipment.modify(this) {
                    it.minus(id).k()
                }
            }
            InfrastructureEquipmentType.SEWAGE_PLANT -> {
                infrastructureSurveyOptics.sewagePlant.equipment.modify(this) {
                    it.minus(id).k()
                }
            }
            InfrastructureEquipmentType.RADIATION_CONTROL -> {
                infrastructureSurveyOptics.radiationControl.equipment.modify(this) {
                    it.minus(id).k()
                }
            }

            InfrastructureEquipmentType.ENVIRONMENT_MONITORING -> {
                infrastructureSurveyOptics.environmentMonitoring.equipment.modify(this) {
                    it.minus(id).k()
                }
            }
        }
    }
}
