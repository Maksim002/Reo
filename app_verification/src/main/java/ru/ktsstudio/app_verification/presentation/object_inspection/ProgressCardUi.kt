package ru.ktsstudio.app_verification.presentation.object_inspection

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.ktsstudio.core_data_verfication_api.data.model.SurveySubtype

/**
 * @author Maxim Ovchinnikov on 20.11.2020.
 */
data class ProgressCardUi(
    val surveySubtype: SurveySubtype,
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val progressText: String
)
