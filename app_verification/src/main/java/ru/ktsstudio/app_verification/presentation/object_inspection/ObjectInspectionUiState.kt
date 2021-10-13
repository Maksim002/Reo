package ru.ktsstudio.app_verification.presentation.object_inspection

/**
 * @author Maxim Ovchinnikov on 20.11.2020.
 */
data class ObjectInspectionUiState(
    val error: Throwable?,
    val isLoading: Boolean,
    val isSending: Boolean,
    val progressList: List<Any>,
    val isReady: Boolean
)
