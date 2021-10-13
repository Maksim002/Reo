package ru.ktsstudio.app_verification.presentation.object_survey.equipment

/**
 * @author Maxim Myalkin (MaxMyalkin) on 10.12.2020.
 */
data class EquipmentSurveyUiState(
    val loading: Boolean,
    val error: Throwable?,
    val data: List<Any>
)
