package ru.ktsstudio.app_verification.presentation.object_survey.equipment

sealed class CommonEquipmentsFields {
    data class Photos(val id: String) : CommonEquipmentsFields()
    data class Passport(val id: String) : CommonEquipmentsFields()
}
