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
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.WeightControl
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.availabilityInfo
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.isAvailable
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableNotAvailableReason
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.weightControl

/**
 * Created by Igor Park on 12/12/2020.
 */
class WeightControlCardMapper(private val resources: ResourceManager) {
    @OptIn(ExperimentalStdlibApi::class)
    fun map(
        weightControl: WeightControl,
        weightControlOptics: Optional<InfrastructureSurveyDraft, WeightControl>,
        isChecked: Boolean
    ): List<Any> {
        val isAvailable = weightControl.availabilityInfo.isAvailable
        return listOf<Any>(
            InnerLabeledSurveyWithCheck(
                label = resources.getString(R.string.survey_infrastructure_has_weight_control_equipment_label),
                checkableValueConsumer = AvailabilityUpdater(
                    isChecked = isChecked,
                    isAvailable = isAvailable,
                    setInfo = { draft, availableState, checkedState ->
                        weightControlOptics.availabilityInfo
                            .isAvailable
                            .set(draft, availableState)
                            .let { updatedDraft ->
                                InfrastructureSurveyDraft
                                    .infrastructureCheckedSurvey
                                    .weightControl
                                    .set(updatedDraft, checkedState)
                            }
                    }
                ),
                backgroundColor = R.color.background_100,
                identifier = WeightControl::class.java.canonicalName
            )
        ) + buildList {
            if (isAvailable) {
                weightControl.takeIf { weightControl.equipment.isNotEmpty() }
                    ?.let { addNestedWeightControlEquipment(weightControl, resources) }
                    ?: add(getEmpty(resources.getString(R.string.survey_equipment_empty)))
                add(
                    getAddButton(
                        InfrastructureEquipmentType.WEIGHT_CONTROL,
                        resources.getString(R.string.survey_equipment_add)
                    )
                )
            } else {
                add(
                    InnerLabeledComment(
                        valueConsumer = NotAvailableReasonUpdater(
                            notAvailableReason = weightControl.availabilityInfo.notAvailableReason,
                            setInfo = { draft, reason ->
                                weightControlOptics.availabilityInfo
                                    .nullableNotAvailableReason
                                    .set(draft, reason)
                            }
                        ),
                        label = resources.getString(R.string.survey_reason_label),
                        editHint = resources.getString(R.string.survey_reason_hint),
                        inputFormat = TextFormat.Text,
                        entityId = WeightControl::class.java.canonicalName
                    )
                )
            }
        }
    }
}
