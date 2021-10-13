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
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.SewagePlant
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.availabilityInfo
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.isAvailable
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableNotAvailableReason
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.sewagePlant
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

/**
 * Created by Igor Park on 12/12/2020.
 */
class SewagePlantCardMapper(private val resources: ResourceManager) {
    @OptIn(ExperimentalStdlibApi::class)
    fun map(
        sewagePlant: SewagePlant,
        sewagePlantTypes: List<Reference>,
        sewagePlantOptics: Optional<InfrastructureSurveyDraft, SewagePlant>,
        isChecked: Boolean
    ): List<Any> {
        val isAvailable = sewagePlant.availabilityInfo.isAvailable
        return listOf<Any>(
            InnerLabeledSurveyWithCheck(
                label = resources.getString(R.string.survey_infrastructure_has_sewage_plant_label),
                checkableValueConsumer = AvailabilityUpdater(
                    isChecked = isChecked,
                    isAvailable = isAvailable,
                    setInfo = { draft, availabilityState, checkedState ->
                        sewagePlantOptics.availabilityInfo
                            .isAvailable
                            .set(draft, availabilityState)
                            .let { updatedDraft ->
                                InfrastructureSurveyDraft.infrastructureCheckedSurvey
                                    .sewagePlant
                                    .set(updatedDraft, checkedState)
                            }
                    }
                ),
                backgroundColor = R.color.background_100,
                identifier = SewagePlant::class.java.canonicalName
            )
        ) + buildList {
            if (isAvailable) {
                sewagePlant.takeIf { sewagePlant.equipment.isNotEmpty() }
                    ?.let {
                        addNestedSewagePlantInfo(
                            sewagePlant = sewagePlant,
                            types = sewagePlantTypes,
                            resources = resources
                        )
                    }
                    ?: add(getEmpty(resources.getString(R.string.survey_equipment_empty)))
                add(
                    getAddButton(
                        InfrastructureEquipmentType.SEWAGE_PLANT,
                        resources.getString(R.string.survey_equipment_add)
                    )
                )
            } else {
                add(
                    InnerLabeledComment(
                        valueConsumer = NotAvailableReasonUpdater(
                            notAvailableReason = sewagePlant.availabilityInfo.notAvailableReason,
                            setInfo = { draft, update ->
                                sewagePlantOptics.availabilityInfo
                                    .nullableNotAvailableReason
                                    .set(draft, update)
                            }
                        ),
                        label = resources.getString(R.string.survey_reason_label),
                        editHint = resources.getString(R.string.survey_reason_hint),
                        inputFormat = TextFormat.Text,
                        entityId = SewagePlant::class.java.canonicalName
                    )
                )
            }
        }
    }
}
