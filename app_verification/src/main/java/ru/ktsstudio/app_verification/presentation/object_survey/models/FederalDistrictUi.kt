package ru.ktsstudio.app_verification.presentation.object_survey.models

import ru.ktsstudio.common.ui.base_spinner.dropdown.UiDropdownItem

data class FederalDistrictUi(
    override val id: String,
    override val title: String,
    override val isSelected: Boolean
) : UiDropdownItem<String>
