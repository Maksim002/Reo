package ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers

import arrow.optics.Optional
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.AvailabilityUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.NotAvailableReasonUpdater
import ru.ktsstudio.app_verification.domain.object_survey.common.data_type.ReferenceUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.SecurityStaffCountUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.infrastructureCheckedSurvey
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledComment
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSelector
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSurveyWithCheck
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.SecurityStation
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.availabilityInfo
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.isAvailable
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableNotAvailableReason
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableSecuritySource
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.securityStation
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

/**
 * Created by Igor Park on 12/12/2020.
 */
class SecurityCardMapper(private val resources: ResourceManager) {
    fun map(
        securityStation: SecurityStation,
        securitySources: List<Reference>,
        isChecked: Boolean,
        securityOptics: Optional<InfrastructureSurveyDraft, SecurityStation>
    ): List<Any> {
        val isAvailable = securityStation.availabilityInfo.isAvailable
        val selectedType = securitySources.find {
            it.id == securityStation.securitySource?.id
        }
        return listOfNotNull(
            InnerLabeledSurveyWithCheck(
                label = resources.getString(R.string.survey_infrastructure_has_security_station_label),
                checkableValueConsumer = AvailabilityUpdater(
                    isChecked = isChecked,
                    isAvailable = isAvailable,
                    setInfo = { draft, availableState, checkedState ->
                        val updated = securityOptics
                            .availabilityInfo
                            .isAvailable
                            .set(draft, availableState)

                        InfrastructureSurveyDraft
                            .infrastructureCheckedSurvey
                            .securityStation
                            .set(updated, checkedState)
                    }
                ),
                backgroundColor = R.color.background_100,
                identifier = SecurityFields.HasSecurity
            ),
            InnerLabeledComment(
                valueConsumer = NotAvailableReasonUpdater(
                    notAvailableReason = securityStation.availabilityInfo.notAvailableReason,
                    setInfo = { draft, reason ->
                        securityOptics.availabilityInfo
                            .nullableNotAvailableReason
                            .set(draft, reason)
                    }
                ),
                label = resources.getString(R.string.survey_reason_label),
                editHint = resources.getString(R.string.survey_reason_hint),
                inputFormat = TextFormat.Text,
                entityId = SecurityFields.NoSecurityReason
            ).takeIf { isAvailable.not() },
            InnerLabeledSelector(
                label = resources.getString(R.string.survey_infrastructure_security_source_label),
                hint = resources.getString(R.string.survey_infrastructure_security_source_hint),
                selectedTitle = selectedType?.name,
                items = securitySources.map {
                    ReferenceUi(
                        id = it.id,
                        title = it.name,
                        isSelected = it.id == selectedType?.id
                    )
                },
                valueConsumer = ReferenceUpdater<InfrastructureSurveyDraft>(
                    reference = selectedType?.id,
                    getReference = { selectedTypeId ->
                        securitySources.find { it.id == selectedTypeId }
                    },
                    setReference = { draft, update ->
                        securityOptics.nullableSecuritySource
                            .set(draft, update)
                    }
                ),
                identifier = SecurityFields.SecuritySource
            ).takeIf { isAvailable },
            InnerLabeledEditItem(
                label = resources.getString(R.string.survey_infrastructure_security_staff_count_label),
                editHint = resources.getString(R.string.survey_infrastructure_security_staff_count_hint),
                inputFormat = TextFormat.Number(),
                valueConsumer = SecurityStaffCountUpdater(
                    staffCount = securityStation.securityStaffCount
                ),
                entityId = SecurityFields.SecurityStaffCount
            ).takeIf { isAvailable },
            EmptySpace(isNested = true)
        )
    }

    private enum class SecurityFields {
        HasSecurity,
        NoSecurityReason,
        SecuritySource,
        SecurityStaffCount
    }
}
