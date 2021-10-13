package ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers

import arrow.optics.Optional
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.data_type.EquipmentPhotoUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.AvailabilityUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.NotAvailableReasonUpdater
import ru.ktsstudio.app_verification.domain.object_survey.common.data_type.ReferenceUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.RoadLengthUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.infrastructureCheckedSurvey
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledComment
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSelector
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSurveyWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.media.LabeledMediaListItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.RoadNetwork
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.availabilityInfo
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.isAvailable
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableNotAvailableReason
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableRoadCoverageType
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableSchema
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.roadNetwork
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

/**
 * Created by Igor Park on 12/12/2020.
 */
class RoadsCardMapper(private val resources: ResourceManager) {
    fun map(
        roads: RoadNetwork,
        roadCoverTypes: List<Reference>,
        isChecked: Boolean,
        roadsOptics: Optional<InfrastructureSurveyDraft, RoadNetwork>
    ): List<Any> {
        val isAvailable = roads.availabilityInfo.isAvailable
        val selectedType = roadCoverTypes.find {
            it.id == roads.roadCoverageType?.id
        }
        return listOfNotNull(
            InnerLabeledSurveyWithCheck(
                label = resources.getString(R.string.survey_infrastructure_has_roads_label),
                checkableValueConsumer = AvailabilityUpdater(
                    isChecked = isChecked,
                    isAvailable = isAvailable,
                    setInfo = { draft, availableState, checkedState ->
                        val updated = roadsOptics
                            .availabilityInfo
                            .isAvailable
                            .set(draft, availableState)

                        InfrastructureSurveyDraft
                            .infrastructureCheckedSurvey
                            .roadNetwork
                            .set(updated, checkedState)
                    }
                ),
                backgroundColor = R.color.background_100,
                identifier = RoadsFields.HasRoadNetwork
            ),
            InnerLabeledComment(
                valueConsumer = NotAvailableReasonUpdater(
                    notAvailableReason = roads.availabilityInfo.notAvailableReason,
                    setInfo = { draft, update ->
                        roadsOptics.availabilityInfo
                            .nullableNotAvailableReason
                            .set(draft, update)
                    }
                ),
                label = resources.getString(R.string.survey_reason_label),
                editHint = resources.getString(R.string.survey_reason_hint),
                inputFormat = TextFormat.Text,
                entityId = RoadsFields.NoRoadNetworkReason
            ).takeIf { isAvailable.not() },
            InnerLabeledSelector(
                label = resources.getString(R.string.survey_infrastructure_roads_type_label),
                hint = resources.getString(R.string.survey_infrastructure_survey_select_type_hint),
                selectedTitle = selectedType?.name,
                items = roadCoverTypes.map {
                    ReferenceUi(
                        id = it.id,
                        title = it.name,
                        isSelected = it.id == selectedType?.id
                    )
                },
                valueConsumer = ReferenceUpdater<InfrastructureSurveyDraft>(
                    reference = selectedType?.id,
                    getReference = { selectedTypeId ->
                        roadCoverTypes.find { it.id == selectedTypeId }
                    },
                    setReference = { draft, update ->
                        roadsOptics.nullableRoadCoverageType
                            .set(draft, update)
                    }
                ),
                identifier = RoadsFields.RoadNetworkType
            ).takeIf { isAvailable },
            InnerLabeledEditItem(
                label = resources.getString(R.string.survey_infrastructure_roads_length_label),
                editHint = resources.getString(R.string.survey_infrastructure_roads_length_hint),
                inputFormat = TextFormat.Number(),
                valueConsumer = RoadLengthUpdater(
                    roadLength = roads.roadLength
                ),
                entityId = RoadsFields.RoadNetworkLength
            ).takeIf { isAvailable },
            LabeledMediaListItem(
                label = resources.getString(R.string.survey_infrastructure_road_scheme_label),
                valueConsumer = EquipmentPhotoUpdater<InfrastructureSurveyDraft>(
                    photos = listOfNotNull(roads.schema),
                    setEquipment = { draft, photos ->
                        roadsOptics.nullableSchema
                            .set(draft, photos.lastOrNull())
                    }
                ),
                identifier = RoadsFields.RoadNetworkSchema,
                isNested = true
            ).takeIf { isAvailable },
            EmptySpace(isNested = true)
        )
    }

    private enum class RoadsFields {
        HasRoadNetwork,
        NoRoadNetworkReason,
        RoadNetworkType,
        RoadNetworkLength,
        RoadNetworkSchema
    }
}
