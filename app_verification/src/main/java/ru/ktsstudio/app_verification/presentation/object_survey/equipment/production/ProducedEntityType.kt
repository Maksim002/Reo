package ru.ktsstudio.app_verification.presentation.object_survey.equipment.production

sealed class ProducedEntityType {
    object Product : ProducedEntityType()
    object Service : ProducedEntityType()
}
