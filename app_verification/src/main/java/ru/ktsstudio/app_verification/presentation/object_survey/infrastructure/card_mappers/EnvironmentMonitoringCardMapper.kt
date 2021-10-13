package ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers

import arrow.optics.Optional
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.AvailabilityUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.NotAvailableReasonUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.infrastructureCheckedSurvey
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledComment
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSurveyWithCheck
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.EnvironmentMonitoring
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.availabilityInfo
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.environmentMonitoring
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.isAvailable
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableNotAvailableReason
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

/**
 * Created by Igor Park on 12/12/2020.
 */
class EnvironmentMonitoringCardMapper(private val resources: ResourceManager) {
    @OptIn(ExperimentalStdlibApi::class)
    fun map(
        environmentMonitoring: EnvironmentMonitoring,
        environmentOptics: Optional<InfrastructureSurveyDraft, EnvironmentMonitoring>,
        environmentMonitoringSystemTypes: List<Reference>,
        isChecked: Boolean
    ): List<Any> {
        val isAvailable = environmentMonitoring.availabilityInfo.isAvailable
        return listOfNotNull<Any>(
            InnerLabeledSurveyWithCheck(
                label = resources.getString(R.string.survey_infrastructure_has_environment_monitoring_label),
                checkableValueConsumer = AvailabilityUpdater(
                    isChecked = isChecked,
                    isAvailable = isAvailable,
                    setInfo = { draft, availableState, checkedState ->
                        val updated = environmentOptics
                            .availabilityInfo
                            .isAvailable
                            .set(draft, availableState)

                        InfrastructureSurveyDraft
                            .infrastructureCheckedSurvey
                            .environmentMonitoring
                            .set(updated, checkedState)
                    }
                ),
                backgroundColor = R.color.background_100,
                identifier = EnvironmentMonitoring::class.java.canonicalName
            ),
        ) + buildList {
            if (isAvailable) {
                environmentMonitoring.takeIf { environmentMonitoring.equipment.isNotEmpty() }
                    ?.let {
                        addNestedEnvironmentMonitoringEquipment(
                            environmentMonitoring,
                            environmentMonitoringSystemTypes,
                            resources
                        )
                    }
                    ?: add(getEmpty(resources.getString(R.string.survey_equipment_empty)))
                add(
                    getAddButton(
                        InfrastructureEquipmentType.ENVIRONMENT_MONITORING,
                        resources.getString(R.string.survey_infrastructure_environment_monitoring_system_add_button)
                    )
                )
            } else {
                add(
                    InnerLabeledComment(
                        valueConsumer = NotAvailableReasonUpdater(
                            notAvailableReason = environmentMonitoring.availabilityInfo
                                .notAvailableReason,
                            setInfo = { draft, update ->
                                environmentOptics.availabilityInfo
                                    .nullableNotAvailableReason
                                    .set(draft, update)
                            }
                        ),
                        label = resources.getString(R.string.survey_reason_label),
                        editHint = resources.getString(R.string.survey_reason_hint),
                        inputFormat = TextFormat.Text,
                        entityId = EnvironmentMonitoring::class.java.canonicalName
                    )
                )
            }
        }
    }
}
