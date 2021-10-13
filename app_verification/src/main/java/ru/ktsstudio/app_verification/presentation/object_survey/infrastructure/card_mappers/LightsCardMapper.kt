package ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers

import arrow.optics.Optional
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.AvailabilityUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.NotAvailableReasonUpdater
import ru.ktsstudio.app_verification.domain.object_survey.common.data_type.ReferenceUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.infrastructureCheckedSurvey
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledComment
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSelector
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSurveyWithCheck
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.LightSystem
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.availabilityInfo
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.isAvailable
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.lightSystem
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableLightSystemType
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableNotAvailableReason
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

/**
 * Created by Igor Park on 12/12/2020.
 */
class LightsCardMapper(private val resources: ResourceManager) {
    fun map(
        lights: LightSystem,
        lightTypes: List<Reference>,
        isChecked: Boolean,
        lightsOptics: Optional<InfrastructureSurveyDraft, LightSystem>
    ): List<Any> {
        val isAvailable = lights.availabilityInfo.isAvailable
        val selectedType = lightTypes.find {
            it.id == lights.lightSystemType?.id
        }
        return listOfNotNull(
            InnerLabeledSurveyWithCheck(
                label = resources.getString(R.string.survey_infrastructure_has_lights_label),
                checkableValueConsumer = AvailabilityUpdater(
                    isChecked = isChecked,
                    isAvailable = isAvailable,
                    setInfo = { draft, availableState, checkedState ->
                        val updated = lightsOptics
                            .availabilityInfo
                            .isAvailable
                            .set(draft, availableState)

                        InfrastructureSurveyDraft
                            .infrastructureCheckedSurvey
                            .lightSystem
                            .set(updated, checkedState)
                    }
                ),
                backgroundColor = R.color.background_100,
                identifier = LightsFields.HasLights
            ),
            InnerLabeledComment(
                valueConsumer = NotAvailableReasonUpdater(
                    notAvailableReason = lights.availabilityInfo.notAvailableReason,
                    setInfo = { draft, reason ->
                        lightsOptics.availabilityInfo
                            .nullableNotAvailableReason
                            .set(draft, reason)
                    }
                ),
                label = resources.getString(R.string.survey_reason_label),
                editHint = resources.getString(R.string.survey_reason_hint),
                inputFormat = TextFormat.Text,
                entityId = LightsFields.NoLightsReason
            ).takeIf { isAvailable.not() },
            InnerLabeledSelector(
                label = resources.getString(R.string.survey_infrastructure_lights_type_label),
                hint = resources.getString(R.string.survey_infrastructure_survey_select_type_hint),
                selectedTitle = selectedType?.name,
                items = lightTypes.map {
                    ReferenceUi(
                        id = it.id,
                        title = it.name,
                        isSelected = it.id == selectedType?.id
                    )
                },
                valueConsumer = ReferenceUpdater<InfrastructureSurveyDraft>(
                    reference = selectedType?.id,
                    getReference = { selectedTypeId ->
                        lightTypes.find { it.id == selectedTypeId }
                    },
                    setReference = { draft, update ->
                        lightsOptics.nullableLightSystemType
                            .set(draft, update)
                    }
                ),
                identifier = LightsFields.LightsType
            ).takeIf { isAvailable },
            EmptySpace(isNested = true)
        )
    }

    private enum class LightsFields {
        HasLights,
        NoLightsReason,
        LightsType
    }
}
