package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import androidx.annotation.ColorRes
import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer

/**
 * @author Maxim Ovchinnikov on 09.12.2020.
 */
data class InnerLabeledSurveyWithCheck(
    val checkableValueConsumer: CheckableValueConsumer<Boolean, *>,
    val label: String,
    @ColorRes val backgroundColor: Int,
    val identifier: Identifier = null
)
