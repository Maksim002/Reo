package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.utils.text_format.TextFormat

/**
 * @author Maxim Ovchinnikov on 09.12.2020.
 */
data class InnerLabeledEditItem(
    val entityId: Any? = null,
    val label: String,
    val editHint: String,
    val inputFormat: TextFormat,
    val enabled: Boolean = true,
    val inCard: Boolean = false,
    val valueConsumer: ValueConsumer<String?, *>,
) {
    val id = "$entityId-$label-$editHint-$inputFormat"
}
