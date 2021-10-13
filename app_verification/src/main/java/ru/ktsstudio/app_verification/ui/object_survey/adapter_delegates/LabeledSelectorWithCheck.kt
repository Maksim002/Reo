package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.common.ui.base_spinner.dropdown.UiDropdownItem

/**
 * Created by Igor Park on 04/12/2020.
 */
data class LabeledSelectorWithCheck<T>(
    val label: String,
    val selectedTitle: String?,
    val hint: String,
    val items: List<UiDropdownItem<T>>,
    val valueConsumer: CheckableValueConsumer<T?, *>
)
