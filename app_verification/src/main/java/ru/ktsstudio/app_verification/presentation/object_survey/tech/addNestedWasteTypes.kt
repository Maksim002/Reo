package ru.ktsstudio.app_verification.presentation.object_survey.tech

import arrow.optics.Optional
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.common.data_type.ReferenceUpdater
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.TechnicalSurveyDraft
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.ReferenceUi
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.DeletableReferenceItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSelector
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.SubtitleItemWithCheck
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

@OptIn(ExperimentalStdlibApi::class)
fun addNestedReceivedWasteTypes(
    initCheckedState: Boolean,
    selectedTypes: List<Reference>,
    listUpdaterOptics: Optional<TechnicalSurveyDraft, List<Reference>>,
    checkUpdaterOptics: Optional<TechnicalSurveyDraft, Boolean>,
    types: List<Reference>,
    resources: ResourceManager
): List<Any> {
    val referenceList = types.filter { selectedTypes.contains(it).not() }

    return buildList {
        add(
            SubtitleItemWithCheck(
                checkableValueConsumer = CheckableTitle<TechnicalSurveyDraft>(
                    isChecked = initCheckedState,
                    updater = { isChecked, draft ->
                        checkUpdaterOptics.set(draft, isChecked)
                    }
                ),
                title = resources.getString(R.string.survey_technical_received_wastes),
                isNested = false,
                withAccent = false
            )
        )
        add(
            InnerLabeledSelector(
                label = "",
                hint = resources.getString(R.string.survey_add),
                selectedTitle = null,
                items = referenceList.map {
                    ReferenceUi(
                        id = it.id,
                        title = it.name,
                        isSelected = false
                    )
                },
                valueConsumer = ReferenceUpdater<TechnicalSurveyDraft>(
                    reference = null,
                    getReference = { refId ->
                        types.find { it.id == refId }
                    },
                    setReference = { draft, reference ->
                        listUpdaterOptics.modify(draft) {
                            it.toMutableList().apply { add(reference) }
                        }
                    }
                ),
                inCard = false,
                identifier = WASTE_CATEGORY_SELECTOR,
                isNested = false
            )
        )
        selectedTypes.forEach { reference ->
            add(
                DeletableReferenceItem(
                    id = reference.id,
                    title = reference.name,
                    valueConsumer = StringValueConsumer<TechnicalSurveyDraft>(
                        value = reference.id,
                        updater = { refId, draft ->
                            listUpdaterOptics.modify(draft) {
                                it.toMutableList().apply {
                                    remove(find { it.id == refId })
                                }
                            }
                        }
                    )
                )
            )
        }
    }
}

private const val WASTE_CATEGORY_SELECTOR = "WASTE_CATEGORY_SELECTOR"
