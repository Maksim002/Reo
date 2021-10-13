package ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers

import arrow.optics.Optional
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.AvailabilityUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.NotAvailableReasonUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.infrastructureCheckedSurvey
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.infrastructureSurvey
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledComment
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSurveyWithCheck
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.SecurityCamera
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.availabilityInfo
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.isAvailable
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableCount
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableNotAvailableReason
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.securityCamera

/**
 * Created by Igor Park on 12/12/2020.
 */
class VideoControlCardMapper(private val resources: ResourceManager) {
    fun map(
        securityCamera: SecurityCamera,
        isChecked: Boolean,
        securityCameraOptics: Optional<InfrastructureSurveyDraft, SecurityCamera>
    ): List<Any> {
        val isAvailable = securityCamera.availabilityInfo.isAvailable
        return listOfNotNull(
            InnerLabeledSurveyWithCheck(
                label = resources.getString(R.string.survey_infrastructure_has_security_camera_label),
                checkableValueConsumer = AvailabilityUpdater(
                    isChecked = isChecked,
                    isAvailable = isAvailable,
                    setInfo = { draft, availabilityState, checkedState ->
                        val updated = securityCameraOptics
                            .availabilityInfo
                            .isAvailable
                            .set(draft, availabilityState)

                        InfrastructureSurveyDraft
                            .infrastructureCheckedSurvey
                            .securityCamera
                            .set(updated, checkedState)
                    }
                ),
                backgroundColor = R.color.background_100,
                identifier = SecurityCamera::class.java.canonicalName
            ),
            InnerLabeledComment(
                valueConsumer = NotAvailableReasonUpdater(
                    notAvailableReason = securityCamera.availabilityInfo.notAvailableReason,
                    setInfo = { draft, update ->
                        securityCameraOptics
                            .availabilityInfo
                            .nullableNotAvailableReason
                            .set(draft, update)
                    }
                ),
                label = resources.getString(R.string.survey_reason_label),
                editHint = resources.getString(R.string.survey_reason_hint),
                inputFormat = TextFormat.Text,
                entityId = SecurityCamera::class.java.canonicalName
            ).takeIf { isAvailable.not() },
            InnerLabeledEditItem(
                label = resources.getString(R.string.survey_equipment_label_count),
                editHint = resources.getString(R.string.survey_infrastructure_count_equipment_hint),
                inputFormat = TextFormat.Number(),
                valueConsumer = StringValueConsumer(
                    value = securityCamera.count?.toString(),
                    updater = { value: String?, draft: InfrastructureSurveyDraft ->
                        InfrastructureSurveyDraft
                            .infrastructureSurvey
                            .securityCamera
                            .nullableCount
                            .set(draft, value?.toIntOrNull())
                    }
                ),
                entityId = SecurityCamera::class.java.canonicalName
            ).takeIf { isAvailable },
            EmptySpace(isNested = true)
        )
    }
}
