package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import ru.ktsstudio.app_verification.ui.common.ValueConsumer

data class DeletableReferenceItem(
    val id: String,
    val title: String,
    val inCard: Boolean = false,
    val valueConsumer: ValueConsumer<String?, *>
)
