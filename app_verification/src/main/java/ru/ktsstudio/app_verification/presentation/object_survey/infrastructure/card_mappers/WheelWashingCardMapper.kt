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
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.WheelsWashing
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.availabilityInfo
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.isAvailable
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableNotAvailableReason
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.wheelsWashing

/**
 * Created by Igor Park on 12/12/2020.
 */
class WheelWashingCardMapper(private val resources: ResourceManager) {
    @OptIn(ExperimentalStdlibApi::class)
    fun map(
        wheelsWashing: WheelsWashing,
        isChecked: Boolean,
        wheelsOptics: Optional<InfrastructureSurveyDraft, WheelsWashing>
    ): List<Any> {
        val isAvailable = wheelsWashing.availabilityInfo.isAvailable
        return listOf<Any>(
            InnerLabeledSurveyWithCheck(
                label = resources.getString(R.string.survey_infrastructure_has_wheel_washing_point_label),
                checkableValueConsumer = AvailabilityUpdater(
                    isChecked = isChecked,
                    isAvailable = isAvailable,
                    setInfo = { draft, availableState, checkedState ->
                        wheelsOptics.availabilityInfo
                            .isAvailable
                            .set(draft, availableState)
                            .let { updatedDraft ->
                                InfrastructureSurveyDraft
                                    .infrastructureCheckedSurvey
                                    .wheelsWashing
                                    .set(updatedDraft, checkedState)
                            }
                    }
                ),
                backgroundColor = R.color.background_100,
                identifier = WheelsWashing::class.java.canonicalName
            )
        ) + buildList {
            if (isAvailable) {
                wheelsWashing.takeIf { wheelsWashing.equipment.isNotEmpty() }
                    ?.let { addNestedWheelsWashingEquipment(wheelsWashing, resources) }
                    ?: add(getEmpty(resources.getString(R.string.survey_equipment_empty)))
                add(
                    getAddButton(
                        InfrastructureEquipmentType.WHEELS_WASHING,
                        resources.getString(R.string.survey_equipment_add)
                    )
                )
            } else {
                add(
                    InnerLabeledComment(
                        valueConsumer = NotAvailableReasonUpdater(
                            notAvailableReason = wheelsWashing.availabilityInfo.notAvailableReason,
                            setInfo = { draft, update ->
                                wheelsOptics.availabilityInfo
                                    .nullableNotAvailableReason
                                    .set(draft, update)
                            }
                        ),
                        label = resources.getString(R.string.survey_reason_label),
                        editHint = resources.getString(R.string.survey_reason_hint),
                        inputFormat = TextFormat.Text,
                        entityId = WheelsWashing::class.java.canonicalName
                    )
                )
            }
        }
    }
}
