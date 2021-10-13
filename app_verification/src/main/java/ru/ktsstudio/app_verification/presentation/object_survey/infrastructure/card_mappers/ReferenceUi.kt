package ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers

import ru.ktsstudio.common.ui.base_spinner.dropdown.UiDropdownItem

/**
 * Created by Igor Park on 12/12/2020.
 */
data class ReferenceUi(
    override val id: String,
    override val title: String,
    override val isSelected: Boolean
) : UiDropdownItem<String>
