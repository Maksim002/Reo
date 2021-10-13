package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.media

import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.Identifier
import ru.ktsstudio.core_data_verfication_api.data.model.Media

/**
 * Created by Igor Park on 16/11/2020.
 */
data class LabeledMediaListItem(
    val label: String,
    val valueConsumer: ValueConsumer<List<Media>, *>,
    val isNested: Boolean = false,
    val inCard: Boolean = false,
    val identifier: Identifier
)
