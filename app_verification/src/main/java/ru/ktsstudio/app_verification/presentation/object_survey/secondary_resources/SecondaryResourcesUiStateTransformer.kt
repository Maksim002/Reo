package ru.ktsstudio.app_verification.presentation.object_survey.secondary_resources

import arrow.core.MapK
import arrow.optics.dsl.at
import arrow.optics.dsl.some
import arrow.optics.extensions.mapk.at.at
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.common.data_type.ReferenceUpdater
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceFeature
import ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.models.SecondaryResourcesDataType
import ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.models.SecondaryResourcesSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.models.secondaryResources
import ru.ktsstudio.app_verification.presentation.object_survey.general.withOtherOption
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.ReferenceUi
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.DeleteEntityItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerEditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSelector
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerMediumTitle
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledEditItemWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LargeTitleItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.SubtitleItemWithCheck
import ru.ktsstudio.common.ui.adapter.delegates.AddEntityItem
import ru.ktsstudio.common.ui.adapter.delegates.CardCornersItem
import ru.ktsstudio.common.ui.adapter.delegates.CardEmptyLine
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.floatValue
import ru.ktsstudio.common.utils.orDefault
import ru.ktsstudio.common.utils.stringValue
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.SecondaryResourcesSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.nullableOtherName
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.nullablePercent
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.nullableReference
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.types

/**
 * @author Maxim Myalkin (MaxMyalkin) on 10.12.2020.
 */
internal class SecondaryResourcesUiStateTransformer(
    private val resourceManager: ResourceManager
) : (Pair<ObjectSurveyFeature.State<SecondaryResourcesSurveyDraft>, ReferenceFeature.State>) ->
SecondaryResourcesSurveyUiState {

    override fun invoke(
        statePair: Pair<ObjectSurveyFeature.State<SecondaryResourcesSurveyDraft>, ReferenceFeature.State>
    ): SecondaryResourcesSurveyUiState {
        val state = statePair.first
        val referenceState = statePair.second
        val references = statePair.second.references
        return SecondaryResourcesSurveyUiState(
            state.loading || referenceState.loading,
            state.error ?: statePair.second.error,
            state.draft?.let { createAdapterItems(it, references) }.orEmpty()
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun createAdapterItems(
        draft: SecondaryResourcesSurveyDraft,
        references: List<Reference>
    ): List<Any> =
        buildList {
            add(LargeTitleItem(resourceManager.getString(R.string.survey_secondary_resources_title)))
            add(
                LabeledEditItemWithCheck(
                    label = resourceManager.getString(R.string.survey_secondary_resources_extract_percent_label),
                    editHint = resourceManager.getString(R.string.survey_equipment_count_hint),
                    inputFormat = TextFormat.NumberPercent,
                    checkableValueConsumer = SecondaryResourcesDataType.ExtractPercent(
                        isChecked = draft.checkedSecondaryResources.extractPercent,
                        value = draft.secondaryResources.extractPercent.stringValue()
                    )
                )
            )
            add(
                SubtitleItemWithCheck(
                    title = resourceManager.getString(R.string.survey_secondary_resources_type_section_label),
                    checkableValueConsumer = SecondaryResourcesDataType.SecondaryResourceTypeConveyor(
                        isChecked = draft.checkedSecondaryResources.types
                    )
                )
            )

            if (draft.secondaryResources.types.isNotEmpty()) {
                addTypes(draft, references)
            } else {
                add(
                    InnerMediumTitle(
                        text = resourceManager.getString(R.string.survey_secondary_resources_type_empty),
                        withAccent = false
                    )
                )
            }
            add(
                AddEntityItem(
                    nested = true,
                    qualifier = SecondaryResourceEntity.Type,
                    text = resourceManager.getString(R.string.survey_add)
                )
            )

            add(getNotFilledPercentElement(draft.secondaryResources))
        }

    private fun getNotFilledPercentElement(survey: SecondaryResourcesSurvey): InnerMediumTitle {
        val extractPercent = survey.extractPercent.orDefault(0f)
        val filledPercent = survey.types.values.map { it.percent.orDefault(0f) }.sum()
        val notFilledPercent = maxOf(0f, extractPercent - filledPercent)
        val label = resourceManager.getString(R.string.survey_secondary_resources_extract_percent_without_types)
        val text = "$label\n${notFilledPercent.stringValue()}"
        return InnerMediumTitle(text)
    }

    private fun MutableList<Any>.addTypes(
        draft: SecondaryResourcesSurveyDraft,
        references: List<Reference>
    ) {
        val lastIndex = draft.secondaryResources.types.size - 1
        draft.secondaryResources.types.values.flatMapIndexed { index: Int, type ->
            val isLast = index == lastIndex
            val referencesWithOtherOption = references.withOtherOption(
                resourceManager.getString(R.string.survey_secondary_resources_other_type)
            )
            listOfNotNull(
                CardCornersItem(isTop = true, isNested = true),
                InnerLabeledSelector(
                    label = resourceManager.getString(R.string.survey_secondary_resources_type_label),
                    hint = resourceManager.getString(R.string.survey_secondary_resources_type_hint),
                    inCard = true,
                    selectedTitle = type.reference?.name,
                    items = referencesWithOtherOption.map {
                        ReferenceUi(
                            id = it.id,
                            title = it.name,
                            isSelected = it.id == type.reference?.id
                        )
                    },
                    valueConsumer = ReferenceUpdater<SecondaryResourcesSurveyDraft>(
                        reference = type.reference?.id,
                        getReference = { selectedId ->
                            referencesWithOtherOption.find { it.id == selectedId }
                        },
                        setReference = { draft, update ->
                            SecondaryResourcesSurveyDraft
                                .secondaryResources
                                .types
                                .at(MapK.at(), type.id)
                                .some
                                .nullableReference
                                .set(draft, update)
                        }
                    ),
                    identifier = type.id
                ),
                InnerEditItem(
                    entityId = type.id,
                    inputFormat = TextFormat.Text,
                    editHint = resourceManager.getString(R.string.survey_name_hint),
                    inCard = true,
                    valueConsumer = StringValueConsumer<SecondaryResourcesSurveyDraft>(
                        value = type.otherName
                    ) { value, updatable ->
                        SecondaryResourcesSurveyDraft.secondaryResources
                            .types
                            .at(MapK.at(), type.id)
                            .some
                            .nullableOtherName
                            .set(updatable, value)
                    }
                ).takeIf { type.reference?.type == ReferenceType.OTHER },
                InnerLabeledEditItem(
                    entityId = type.id,
                    inCard = true,
                    label = resourceManager.getString(R.string.survey_secondary_resources_type_percent_label),
                    editHint = resourceManager.getString(R.string.survey_equipment_count_hint),
                    inputFormat = TextFormat.NumberPercent,
                    valueConsumer = StringValueConsumer<SecondaryResourcesSurveyDraft>(
                        value = type.percent.stringValue()
                    ) { value, updatable ->
                        SecondaryResourcesSurveyDraft.secondaryResources
                            .types
                            .at(MapK.at(), type.id)
                            .some
                            .nullablePercent
                            .set(updatable, value?.floatValue())
                    }
                ),
                CardEmptyLine(
                    height = resourceManager.getDimensionPixelSize(R.dimen.default_padding),
                    horizontalPadding = resourceManager.getDimensionPixelSize(R.dimen.default_side_padding),
                    isNested = true
                ),
                DeleteEntityItem(
                    id = type.id,
                    inCard = true,
                    qualifier = SecondaryResourceEntity.Type
                ),
                CardCornersItem(isTop = false, isNested = true),
                EmptySpace(isNested = true).takeUnless { isLast }
            )
        }.let(::addAll)
    }
}
