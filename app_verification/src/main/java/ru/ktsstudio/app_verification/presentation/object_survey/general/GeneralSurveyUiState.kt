package ru.ktsstudio.app_verification.presentation.object_survey.general

/**
 * Created by Igor Park on 04/12/2020.
 */
data class GeneralSurveyUiState(
    val loading: Boolean,
    val error: Throwable?,
    val data: List<Any>
)
