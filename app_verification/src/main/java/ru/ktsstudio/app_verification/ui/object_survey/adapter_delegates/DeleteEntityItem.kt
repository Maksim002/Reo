package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

data class DeleteEntityItem<EntityQualifier>(
    val id: String,
    val inCard: Boolean = false,
    val qualifier: EntityQualifier
)
