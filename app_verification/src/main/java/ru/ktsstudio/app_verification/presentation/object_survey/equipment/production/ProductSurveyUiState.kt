package ru.ktsstudio.app_verification.presentation.object_survey.equipment.production

data class ProductSurveyUiState(
    val loading: Boolean,
    val error: Throwable?,
    val data: List<Any>
)
