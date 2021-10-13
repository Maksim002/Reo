package ru.ktsstudio.app_verification.presentation.object_survey.schedule

/**
 * @author Maxim Ovchinnikov on 27.11.2020.
 */
data class ObjectScheduleSurveyUiState(
    val loading: Boolean,
    val error: Throwable?,
    val data: List<Any>
)
