package ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers

import arrow.optics.Optional
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.AsuFunctionsUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.AsuNameUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.AvailabilityUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.NotAvailableReasonUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.infrastructureCheckedSurvey
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledComment
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSurveyWithCheck
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.Asu
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.asu
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.availabilityInfo
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.isAvailable
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableNotAvailableReason

/**
 * Created by Igor Park on 12/12/2020.
 */
class AsuCardMapper(private val resources: ResourceManager) {
    fun map(
        asu: Asu,
        isChecked: Boolean,
        asuOptics: Optional<InfrastructureSurveyDraft, Asu>
    ): List<Any> {
        val isAvailable = asu.availabilityInfo.isAvailable
        return listOfNotNull(
            InnerLabeledSurveyWithCheck(
                label = resources.getString(R.string.survey_infrastructure_has_asu_label),
                checkableValueConsumer = AvailabilityUpdater(
                    isChecked = isChecked,
                    isAvailable = isAvailable,
                    setInfo = { draft, availableState, checkedState ->
                        val updated = asuOptics
                            .availabilityInfo
                            .isAvailable
                            .set(draft, availableState)

                        InfrastructureSurveyDraft
                            .infrastructureCheckedSurvey
                            .asu
                            .set(updated, checkedState)
                    }
                ),
                backgroundColor = R.color.background_100,
                identifier = AsuFields.HasAsu
            ),
            InnerLabeledComment(
                valueConsumer = NotAvailableReasonUpdater(
                    notAvailableReason = asu.availabilityInfo.notAvailableReason,
                    setInfo = { draft, update ->
                        asuOptics.availabilityInfo
                            .nullableNotAvailableReason
                            .set(draft, update)
                    }
                ),
                label = resources.getString(R.string.survey_reason_label),
                editHint = resources.getString(R.string.survey_reason_hint),
                inputFormat = TextFormat.Text,
                entityId = AsuFields.NoAsuReason
            ).takeIf { isAvailable.not() },
            InnerLabeledEditItem(
                label = resources.getString(R.string.survey_infrastructure_asu_name_label),
                editHint = resources.getString(R.string.survey_infrastructure_security_staff_count_hint),
                inputFormat = TextFormat.Text,
                valueConsumer = AsuNameUpdater(
                    name = asu.systemName
                ),
                entityId = AsuFields.AsuName
            ).takeIf { isAvailable },
            InnerLabeledComment(
                label = resources.getString(R.string.survey_infrastructure_asu_functions_label),
                editHint = resources.getString(R.string.survey_infrastructure_asu_functions_hint),
                inputFormat = TextFormat.Text,
                valueConsumer = AsuFunctionsUpdater(
                    functions = asu.systemFunctions
                ),
                entityId = AsuFields.AsuFunctions
            ).takeIf { isAvailable },
            EmptySpace(isNested = true)
        )
    }

    private enum class AsuFields {
        HasAsu,
        NoAsuReason,
        AsuName,
        AsuFunctions
    }
}
