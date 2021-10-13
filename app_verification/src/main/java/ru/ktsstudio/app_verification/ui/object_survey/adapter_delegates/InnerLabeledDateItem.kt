package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import org.threeten.bp.Instant
import ru.ktsstudio.app_verification.ui.common.ValueConsumer

/**
 * @author Maxim Ovchinnikov on 15.01.2021.
 */
data class InnerLabeledDateItem(
    val entityId: Any? = null,
    val label: String,
    val editHint: String,
    val inCard: Boolean = false,
    val valueConsumer: ValueConsumer<Instant?, *>,
) {
    val id = "$entityId-$label-$editHint"
}
