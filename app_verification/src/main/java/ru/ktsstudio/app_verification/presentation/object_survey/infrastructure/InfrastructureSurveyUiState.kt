package ru.ktsstudio.app_verification.presentation.object_survey.infrastructure

data class InfrastructureSurveyUiState(
    val loading: Boolean,
    val error: Throwable?,
    val data: List<Any>
)
