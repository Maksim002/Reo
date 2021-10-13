package ru.ktsstudio.app_verification.presentation.object_survey.general

import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueCheckableConsumer
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.general.GeneralSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.general.models.AddressDescriptionType
import ru.ktsstudio.app_verification.domain.object_survey.general.models.FiasAddressType
import ru.ktsstudio.app_verification.domain.object_survey.general.models.GeneralSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.general.models.OtherObjectStatus
import ru.ktsstudio.app_verification.domain.object_survey.general.models.generalCheckedSurvey
import ru.ktsstudio.app_verification.domain.object_survey.general.models.information
import ru.ktsstudio.app_verification.domain.object_survey.common.data_type.ReferenceCheckableUpdater
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.ReferenceUi
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledEditItemWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledMultilineItemWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledSelectorWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LargeTitleItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import ru.ktsstudio.core_data_verfication_api.data.model.name
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType
import ru.ktsstudio.core_data_verfication_api.data.model.subject

/**
 * Created by Igor Park on 04/12/2020.
 */
internal class GeneralSurveyUiStateTransformer(
    private val resourceManager: ResourceManager,
    private val objectTypeUiMapper: Mapper<VerificationObjectType, String>
) : (GeneralSurveyFeature.State) -> GeneralSurveyUiState {

    override fun invoke(state: GeneralSurveyFeature.State): GeneralSurveyUiState {

        return GeneralSurveyUiState(
            loading = state.loading,
            error = state.error,
            data = state.surveyDraft?.let(::createAdapterItems) ?: emptyList()
        )
    }

    private fun createAdapterItems(surveyDraft: GeneralSurveyDraft): List<Any> {
        val statusesWithOtherOption = surveyDraft.objectStatuses.withOtherOption(
            resourceManager.getString(R.string.survey_other_status_name)
        )

        return listOfNotNull(
            LargeTitleItem(resourceManager.getString(R.string.object_inspection_card_label_main_info)),
            LabeledEditItemWithCheck(
                label = resourceManager.getString(R.string.survey_general_object_name_label),
                checkableValueConsumer = StringValueCheckableConsumer<GeneralSurveyDraft>(
                    isChecked = surveyDraft.generalCheckedSurvey.name,
                    value = surveyDraft.information.name,
                    updater = { name, isChecked, draft ->
                        val updated = GeneralSurveyDraft
                            .information
                            .name
                            .set(draft, name.orEmpty())

                        GeneralSurveyDraft
                            .generalCheckedSurvey
                            .name
                            .set(updated, isChecked)
                    }
                ),
                editHint = resourceManager.getString(R.string.survey_name_hint),
                inputFormat = TextFormat.Text
            ),
            EditItem(
                valueConsumer = StringValueConsumer<GeneralSurveyDraft>(
                    value = objectTypeUiMapper.map(surveyDraft.type),
                    updater = { _, draft -> draft }
                ),
                label = resourceManager.getString(R.string.survey_general_object_category_label),
                editHint = "",
                inputFormat = TextFormat.Text,
                isEditable = false
            ),
            LabeledSelectorWithCheck(
                label = resourceManager.getString(R.string.survey_general_object_status_label),
                hint = resourceManager.getString(R.string.survey_infrastructure_survey_select_type_hint),
                selectedTitle = if (surveyDraft.hasOtherObjectStatus) {
                    statusesWithOtherOption.last().name
                } else {
                    surveyDraft.objectStatus?.name
                },
                items = statusesWithOtherOption.map {
                    ReferenceUi(
                        id = it.id,
                        title = it.name,
                        isSelected = it.id == surveyDraft.objectStatus?.id ||
                            surveyDraft.hasOtherObjectStatus && it.type == ReferenceType.OTHER
                    )
                },
                valueConsumer = ReferenceCheckableUpdater(
                    reference = surveyDraft.objectStatus?.id,
                    isChecked = surveyDraft.generalCheckedSurvey.objectStatus,
                    getReference = { selectedStatusId ->
                        statusesWithOtherOption.find { it.id == selectedStatusId }
                    },
                    setReference = { draft: GeneralSurveyDraft,
                        objectStatus: Reference,
                        isChecked: Boolean ->
                        val isOtherStatusName = objectStatus.type == ReferenceType.OTHER
                        draft.copy(
                            objectStatus = objectStatus,
                            hasOtherObjectStatus = isOtherStatusName,
                            generalCheckedSurvey = draft.generalCheckedSurvey.copy(
                                objectStatus = isChecked
                            )
                        )
                    }
                )
            ),
            EditItem(
                valueConsumer = OtherObjectStatus(value = surveyDraft.otherStatusName),
                editHint = resourceManager.getString(R.string.survey_other_status_hint),
                inputFormat = TextFormat.Text
            ).takeIf { surveyDraft.hasOtherObjectStatus },
            LabeledSelectorWithCheck(
                label = resourceManager.getString(R.string.survey_general_federal_district_label),
                valueConsumer = ReferenceCheckableUpdater(
                    reference = surveyDraft.information.subject.id,
                    isChecked = surveyDraft.generalCheckedSurvey.subject,
                    getReference = { selectedDistrictId ->
                        surveyDraft.subjects
                            .find { it.id == selectedDistrictId }
                    },
                    setReference = { draft: GeneralSurveyDraft,
                        district: Reference,
                        isChecked: Boolean ->
                        GeneralSurveyDraft
                            .information
                            .subject
                            .set(draft, district)
                            .copy(
                                generalCheckedSurvey = draft.generalCheckedSurvey.copy(subject = isChecked)
                            )
                    }
                ),
                hint = resourceManager.getString(R.string.survey_choose_hint),
                items = surveyDraft.subjects.map {
                    ReferenceUi(
                        id = it.id,
                        title = it.name,
                        isSelected = surveyDraft.information.subject.id == it.id
                    )
                },
                selectedTitle = surveyDraft.information.subject.name
            ),
            LabeledEditItemWithCheck(
                label = resourceManager.getString(R.string.survey_general_fias_address_label),
                checkableValueConsumer = FiasAddressType(
                    isChecked = surveyDraft.generalCheckedSurvey.fiasAddress,
                    value = surveyDraft.information.fiasAddress?.fullAddress.orEmpty()
                ),
                editHint = resourceManager.getString(R.string.survey_choose_hint),
                inputFormat = TextFormat.Text,
                enabled = false
            ),
            LabeledMultilineItemWithCheck(
                label = resourceManager.getString(R.string.survey_general_address_description_label),
                valueConsumer = AddressDescriptionType(
                    isChecked = surveyDraft.generalCheckedSurvey.addressDescription,
                    value = surveyDraft.information.addressDescription
                ),
                editHint = resourceManager.getString(R.string.survey_general_address_description_hint),
                inputFormat = TextFormat.Text
            )
        )
    }
}
