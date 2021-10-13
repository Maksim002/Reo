package ru.ktsstudio.app_verification.presentation.object_survey.models

import ru.ktsstudio.common.ui.base_spinner.dropdown.UiDropdownItem

data class ObjectStatusUi(
    override val id: Long,
    override val title: String,
    override val isSelected: Boolean
) : UiDropdownItem<Long>
