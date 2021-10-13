package ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers

import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerMediumTitle
import ru.ktsstudio.common.ui.adapter.delegates.AddEntityItem

/**
 * Created by Igor Park on 19/12/2020.
 */

fun getEmpty(text: String): InnerMediumTitle {
    return InnerMediumTitle(
        text = text,
        withAccent = false
    )
}

fun <Qualifier> getAddButton(type: Qualifier, text: String): AddEntityItem<Qualifier> {
    return AddEntityItem(
        nested = true,
        text = text,
        qualifier = type
    )
}
