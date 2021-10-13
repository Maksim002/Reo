package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.ui.base_spinner.dropdown.UiDropdownItem

data class InnerLabeledSelector<T>(
    val label: String,
    val selectedTitle: String?,
    val hint: String,
    val inCard: Boolean = false,
    val items: List<UiDropdownItem<T>>,
    val valueConsumer: ValueConsumer<T?, *>,
    val identifier: Identifier = null,
    val isNested: Boolean = true
)
