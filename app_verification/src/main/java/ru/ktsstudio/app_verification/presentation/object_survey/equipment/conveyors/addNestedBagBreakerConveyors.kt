package ru.ktsstudio.app_verification.presentation.object_survey.equipment.conveyors

import arrow.core.MapK
import arrow.optics.dsl.at
import arrow.optics.dsl.some
import arrow.optics.extensions.mapk.at.at
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.EquipmentSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.equipment
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.EquipmentEntity
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.addNestedCommonEquipmentInfo
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.DeleteEntityItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.common.ui.adapter.delegates.CardCornersItem
import ru.ktsstudio.common.ui.adapter.delegates.CardEmptyLine
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.core_data_verfication_api.data.model.bagBreakers
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ConveyorType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.commonEquipmentInfo

fun MutableList<Any>.addNestedBagBreakerConveyors(item: EquipmentSurveyDraft, resourceManager: ResourceManager) {
    val lastIndex = item.equipment.bagBreakers.values.size - 1
    item.equipment.bagBreakers.values.forEachIndexed { index, conveyor ->
        val isLast = index == lastIndex
        add(CardCornersItem(isTop = true, isNested = true))
        addNestedCommonEquipmentInfo(
            equipmentId = conveyor.id,
            info = conveyor.commonEquipmentInfo,
            updaterOptics = EquipmentSurveyDraft.equipment
                .bagBreakers
                .at(MapK.at(), conveyor.id)
                .some
                .commonEquipmentInfo,
            resourceManager = resourceManager
        )
        add(
            CardEmptyLine(
                height = resourceManager.getDimensionPixelSize(R.dimen.default_padding),
                horizontalPadding = resourceManager.getDimensionPixelSize(R.dimen.default_side_padding),
                isNested = true
            )
        )
        add(
            DeleteEntityItem(
                inCard = true,
                id = conveyor.id,
                qualifier = EquipmentEntity.Conveyor(ConveyorType.BAG_BREAKER)
            )
        )
        add(CardCornersItem(isTop = false, isNested = true))
        EmptySpace(isNested = true).takeUnless { isLast }?.also(::add)
    }
}
