package ru.ktsstudio.app_verification.domain.object_survey.infrastructure

import arrow.core.k
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectAdder
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.infrastructureSurvey
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.InfrastructureEquipmentType
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.EnvironmentMonitoringEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.SewagePlantEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.WeightControlEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.environmentMonitoring
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.equipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.radiationControl
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.sewagePlant
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.weightControl
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.wheelsWashing
import java.util.UUID

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.12.2020.
 */
class InfrastructureEquipmentAdder : NestedObjectAdder<InfrastructureSurveyDraft, InfrastructureEquipmentType> {
    override fun invoke(
        draft: InfrastructureSurveyDraft,
        entityType: InfrastructureEquipmentType
    ): InfrastructureSurveyDraft {
        val id = generateId()
        return draft.addNewEquipment(id, entityType)
    }

    private fun generateId(): String = UUID.randomUUID().toString()

    private fun InfrastructureSurveyDraft.addNewEquipment(
        id: String,
        type: InfrastructureEquipmentType
    ): InfrastructureSurveyDraft {
        val infrastructureSurveyOptics = InfrastructureSurveyDraft.infrastructureSurvey
        return when (type) {
            InfrastructureEquipmentType.WHEELS_WASHING -> {
                infrastructureSurveyOptics.wheelsWashing.equipment.modify(this) {
                    it.plus(id to InfrastructureEquipment.createEmpty(id = id)).k()
                }
            }
            InfrastructureEquipmentType.WEIGHT_CONTROL -> {
                infrastructureSurveyOptics.weightControl.equipment.modify(this) {
                    it.plus(id to WeightControlEquipment.createEmpty(id = id)).k()
                }
            }
            InfrastructureEquipmentType.SEWAGE_PLANT -> {
                infrastructureSurveyOptics.sewagePlant.equipment.modify(this) {
                    it.plus(id to SewagePlantEquipment.createEmpty(id = id)).k()
                }
            }
            InfrastructureEquipmentType.RADIATION_CONTROL -> {
                infrastructureSurveyOptics.radiationControl.equipment.modify(this) {
                    it.plus(id to InfrastructureEquipment.createEmpty(id = id)).k()
                }
            }

            InfrastructureEquipmentType.ENVIRONMENT_MONITORING -> {
                infrastructureSurveyOptics.environmentMonitoring.equipment.modify(this) {
                    it.plus(id to EnvironmentMonitoringEquipment.createEmpty(id = id)).k()
                }
            }
        }
    }
}
