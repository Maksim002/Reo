package ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers

import arrow.optics.Optional
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.data_type.EquipmentPhotoUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.AvailabilityUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.NotAvailableReasonUpdater
import ru.ktsstudio.app_verification.domain.object_survey.common.data_type.ReferenceUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.infrastructureCheckedSurvey
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledComment
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSelector
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSurveyWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.media.LabeledMediaListItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.Fences
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.availabilityInfo
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.fencePhotos
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.fences
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.isAvailable
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableFenceType
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableNotAvailableReason
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

/**
 * Created by Igor Park on 12/12/2020.
 */
class FencesCardMapper(private val resources: ResourceManager) {
    fun map(
        fences: Fences,
        fenceTypes: List<Reference>,
        isChecked: Boolean,
        fencesOptics: Optional<InfrastructureSurveyDraft, Fences>
    ): List<Any> {
        val isAvailable = fences.availabilityInfo.isAvailable
        val selectedType = fenceTypes.find {
            it.id == fences.fenceType?.id
        }
        return listOfNotNull(
            InnerLabeledSurveyWithCheck(
                label = resources.getString(R.string.survey_infrastructure_has_fences_label),
                checkableValueConsumer = AvailabilityUpdater(
                    isChecked = isChecked,
                    isAvailable = isAvailable,
                    setInfo = { draft, availabilityState, checkedState: Boolean ->
                        val updated = fencesOptics
                            .availabilityInfo
                            .isAvailable
                            .set(draft, availabilityState)

                        InfrastructureSurveyDraft
                            .infrastructureCheckedSurvey
                            .fences
                            .set(updated, checkedState)
                    }
                ),
                backgroundColor = R.color.background_100,
                identifier = FencesFields.HasFencesNetwork
            ),
            InnerLabeledComment(
                valueConsumer = NotAvailableReasonUpdater(
                    notAvailableReason = fences.availabilityInfo.notAvailableReason,
                    setInfo = { draft, update ->
                        fencesOptics.availabilityInfo
                            .nullableNotAvailableReason
                            .set(draft, update)
                    }
                ),
                label = resources.getString(R.string.survey_reason_label),
                editHint = resources.getString(R.string.survey_reason_hint),
                inputFormat = TextFormat.Text,
                entityId = FencesFields.NoFencesReason
            ).takeIf { isAvailable.not() },
            InnerLabeledSelector(
                label = resources.getString(R.string.survey_infrastructure_fences_type_label),
                hint = resources.getString(R.string.survey_infrastructure_survey_select_type_hint),
                selectedTitle = selectedType?.name,
                items = fenceTypes.map {
                    ReferenceUi(
                        id = it.id,
                        title = it.name,
                        isSelected = it.id == selectedType?.id
                    )
                },
                valueConsumer = ReferenceUpdater<InfrastructureSurveyDraft>(
                    reference = selectedType?.id,
                    getReference = { selectedTypeId ->
                        fenceTypes.find { it.id == selectedTypeId }
                    },
                    setReference = { draft, update ->
                        fencesOptics.nullableFenceType
                            .set(draft, update)
                    }
                ),
                identifier = FencesFields.FencesType
            ).takeIf { isAvailable },
            LabeledMediaListItem(
                isNested = true,
                label = resources.getString(R.string.survey_infrastructure_fences_photos_label),
                valueConsumer = EquipmentPhotoUpdater<InfrastructureSurveyDraft>(
                    photos = fences.fencePhotos,
                    setEquipment = { draft, photos ->
                        fencesOptics.fencePhotos
                            .set(draft, photos)
                    }
                ),
                identifier = FencesFields.FencePhotos
            ).takeIf { isAvailable },
            EmptySpace(isNested = true)
        )
    }

    private enum class FencesFields {
        HasFencesNetwork,
        NoFencesReason,
        FencesType,
        FencePhotos
    }
}
