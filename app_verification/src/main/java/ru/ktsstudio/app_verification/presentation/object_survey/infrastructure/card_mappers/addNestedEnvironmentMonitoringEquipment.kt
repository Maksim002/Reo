package ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers

import arrow.core.MapK
import arrow.optics.dsl.at
import arrow.optics.dsl.some
import arrow.optics.extensions.mapk.at.at
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.common.data_type.ReferenceUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.infrastructureSurvey
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.DeleteEntityItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSelector
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerMediumTitle
import ru.ktsstudio.common.ui.adapter.delegates.CardCornersItem
import ru.ktsstudio.common.ui.adapter.delegates.CardEmptyLine
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.EnvironmentMonitoring
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.environmentMonitoring
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.equipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableCount
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.systemTypeUsed
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

/**
 * Created by Igor Park on 11/01/2021.
 */
fun MutableList<Any>.addNestedEnvironmentMonitoringEquipment(
    item: EnvironmentMonitoring,
    types: List<Reference>,
    resources: ResourceManager
) {
    val lastIndex = item.equipment.values.size - 1
    add(
        InnerMediumTitle(
            text = resources.getString(R.string.survey_infrastructure_environment_monitoring_system_equipment_title),
            identifier = EnvironmentMonitoring::class.java.canonicalName
        )
    )
    add(EmptySpace(isNested = true))
    add(
        InnerLabeledEditItem(
            label = resources.getString(R.string.survey_infrastructure_environment_monitoring_system_count_label),
            editHint = resources.getString(R.string.survey_equipment_count_hint),
            inputFormat = TextFormat.Number(),
            valueConsumer = StringValueConsumer<InfrastructureSurveyDraft>(
                value = item.count?.toString().orEmpty()
            ) { value, updatable ->
                InfrastructureSurveyDraft.infrastructureSurvey
                    .environmentMonitoring
                    .nullableCount
                    .set(updatable, value?.toIntOrNull())
            }
        )
    )
    add(EmptySpace(isNested = true))
    item.equipment.values.forEachIndexed { index, equipment ->
        val isLast = index == lastIndex
        val selectedType = types.find { it.id == equipment.systemTypeUsed?.id }

        add(CardCornersItem(isTop = true, isNested = true))
        add(
            InnerLabeledSelector(
                label = resources.getString(R.string.survey_infrastructure_environment_monitoring_system_type_label),
                hint = resources.getString(R.string.survey_infrastructure_survey_select_type_hint),
                selectedTitle = selectedType?.name,
                items = types.map {
                    ReferenceUi(
                        id = it.id,
                        title = it.name,
                        isSelected = it.id == selectedType?.id
                    )
                },
                valueConsumer = ReferenceUpdater<InfrastructureSurveyDraft>(
                    reference = selectedType?.id,
                    getReference = { selectedTypeId ->
                        types.find { it.id == selectedTypeId }
                    },
                    setReference = { draft, update ->
                        InfrastructureSurveyDraft.infrastructureSurvey
                            .environmentMonitoring
                            .equipment
                            .at(MapK.at(), equipment.id)
                            .some
                            .systemTypeUsed
                            .set(draft, update)
                    }
                ),
                inCard = true,
                identifier = equipment.id
            )
        )
        add(
            CardEmptyLine(
                height = resources.getDimensionPixelSize(R.dimen.default_padding),
                horizontalPadding = resources.getDimensionPixelSize(R.dimen.default_side_padding),
                isNested = true
            )
        )
        add(
            DeleteEntityItem(
                id = equipment.id,
                inCard = true,
                qualifier = InfrastructureEquipmentType.ENVIRONMENT_MONITORING
            )
        )
        add(CardCornersItem(isTop = false, isNested = true))

        if (isLast.not()) add(EmptySpace(isNested = true))
    }
}
