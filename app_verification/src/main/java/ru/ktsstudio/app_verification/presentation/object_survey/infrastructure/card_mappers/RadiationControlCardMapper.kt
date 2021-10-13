package ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers

import arrow.optics.Optional
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.AvailabilityUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.NotAvailableReasonUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.infrastructureCheckedSurvey
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledComment
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSurveyWithCheck
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.RadiationControl
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.availabilityInfo
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.isAvailable
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableNotAvailableReason
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.radiationControl

/**
 * Created by Igor Park on 12/12/2020.
 */
class RadiationControlCardMapper(private val resources: ResourceManager) {
    @OptIn(ExperimentalStdlibApi::class)
    fun map(
        radiationControl: RadiationControl,
        isChecked: Boolean,
        radiationControlOptics: Optional<InfrastructureSurveyDraft, RadiationControl>
    ): List<Any> {
        val isAvailable = radiationControl.availabilityInfo.isAvailable
        return listOf<Any>(
            InnerLabeledSurveyWithCheck(
                label = resources.getString(R.string.survey_infrastructure_has_radiation_control_label),
                checkableValueConsumer = AvailabilityUpdater(
                    isChecked = isChecked,
                    isAvailable = isAvailable,
                    setInfo = { draft, availabilityState, checkedState ->
                        radiationControlOptics.availabilityInfo
                            .isAvailable
                            .set(draft, availabilityState)
                            .let { updatedDraft ->
                                InfrastructureSurveyDraft.infrastructureCheckedSurvey
                                    .radiationControl
                                    .set(updatedDraft, checkedState)
                            }
                    }
                ),
                backgroundColor = R.color.background_100,
                identifier = RadiationControl::class.java.canonicalName
            )
        ) + buildList {
            if (isAvailable) {
                radiationControl.takeIf { radiationControl.equipment.isNotEmpty() }
                    ?.let { addNestedRadiationControlEquipment(radiationControl, resources) }
                    ?: add(getEmpty(resources.getString(R.string.survey_equipment_empty)))
                add(
                    getAddButton(
                        InfrastructureEquipmentType.RADIATION_CONTROL,
                        resources.getString(R.string.survey_equipment_add)
                    )
                )
            } else {
                add(
                    InnerLabeledComment(
                        valueConsumer = NotAvailableReasonUpdater(
                            notAvailableReason = radiationControl.availabilityInfo.notAvailableReason,
                            setInfo = { draft, update ->
                                radiationControlOptics.availabilityInfo
                                    .nullableNotAvailableReason
                                    .set(draft, update)
                            }
                        ),
                        label = resources.getString(R.string.survey_reason_label),
                        editHint = resources.getString(R.string.survey_reason_hint),
                        inputFormat = TextFormat.Text,
                        entityId = RadiationControl::class.java.canonicalName
                    )
                )
                add(EmptySpace(isNested = true))
            }
        }
    }
}
