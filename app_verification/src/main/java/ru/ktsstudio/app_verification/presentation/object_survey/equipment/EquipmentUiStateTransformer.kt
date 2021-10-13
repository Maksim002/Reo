package ru.ktsstudio.app_verification.presentation.object_survey.equipment

import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceFeature
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.EquipmentSurveyDraft
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.additional.EquipmentAdditionalUiMapper
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.conveyors.EquipmentConveyorUiMapper
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.presses.EquipmentPressUiMapper
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.separators.EquipmentSeparatorUiMapper
import ru.ktsstudio.app_verification.presentation.object_survey.general.withOtherOption
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType

/**
 * @author Maxim Myalkin (MaxMyalkin) on 10.12.2020.
 */
internal class EquipmentUiStateTransformer(
    private val resourceManager: ResourceManager
) : (Pair<ObjectSurveyFeature.State<EquipmentSurveyDraft>, ReferenceFeature.State>) -> EquipmentSurveyUiState {

    private val conveyorMapper = EquipmentConveyorUiMapper(resourceManager)
    private val separatorMapper = EquipmentSeparatorUiMapper(resourceManager)
    private val pressMapper = EquipmentPressUiMapper(resourceManager)
    private val additionalEquipmentMapper = EquipmentAdditionalUiMapper(resourceManager)

    override fun invoke(
        surveyWithReferences: Pair<ObjectSurveyFeature.State<EquipmentSurveyDraft>, ReferenceFeature.State>
    ): EquipmentSurveyUiState {
        val surveyState = surveyWithReferences.first
        val referenceFeatureState = surveyWithReferences.second
        val additionalEquipmentReferences = referenceFeatureState.references
            .filter { it.type == ReferenceType.ADDITIONAL_EQUIPMENT }

        return EquipmentSurveyUiState(
            loading = surveyState.loading || referenceFeatureState.loading,
            error = surveyState.error ?: referenceFeatureState.error,
            data = surveyState.draft
                ?.let { draft -> createAdapterItems(draft, additionalEquipmentReferences) }
                .orEmpty()
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun createAdapterItems(
        draft: EquipmentSurveyDraft,
        references: List<Reference>
    ): List<Any> = buildList {
        addAll(conveyorMapper.map(draft))
        addAll(separatorMapper.map(draft))
        addAll(pressMapper.map(draft))
        addAll(
            additionalEquipmentMapper.map(
                draft,
                references.withOtherOption(
                    resourceManager.getString(R.string.survey_equipment_additional_other_title)
                )
            )
        )
    }
}
