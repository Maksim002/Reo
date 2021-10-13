package ru.ktsstudio.app_verification.presentation.object_survey.secondary_resources

/**
 * @author Maxim Myalkin (MaxMyalkin) on 10.12.2020.
 */
data class SecondaryResourcesSurveyUiState(
    val loading: Boolean,
    val error: Throwable?,
    val data: List<Any>
)
