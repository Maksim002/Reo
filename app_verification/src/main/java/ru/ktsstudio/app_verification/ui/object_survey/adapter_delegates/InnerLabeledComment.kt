package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.utils.text_format.TextFormat

/**
 * @author Maxim Ovchinnikov on 09.12.2020.
 */
data class InnerLabeledComment(
    val entityId: Any? = null,
    val label: String,
    val editHint: String,
    val inCard: Boolean = false,
    val inputFormat: TextFormat,
    val valueConsumer: ValueConsumer<String?, *>,
) {
    val id = "$entityId-$label-$editHint-$inputFormat"
}
