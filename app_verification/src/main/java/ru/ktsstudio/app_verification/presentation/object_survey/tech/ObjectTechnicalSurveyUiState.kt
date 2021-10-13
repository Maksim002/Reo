package ru.ktsstudio.app_verification.presentation.object_survey.tech

/**
 * @author Maxim Ovchinnikov on 07.12.2020.
 */
data class ObjectTechnicalSurveyUiState(
    val loading: Boolean,
    val error: Throwable?,
    val data: List<Any>
)
