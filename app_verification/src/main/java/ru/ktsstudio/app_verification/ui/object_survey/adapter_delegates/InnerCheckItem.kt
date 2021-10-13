package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import ru.ktsstudio.app_verification.ui.common.ValueConsumer

/**
 * Created by Igor Park on 09/02/2021.
 */
data class InnerCheckItem(
    val title: String,
    val valueConsumer: ValueConsumer<Boolean?, *>
)
