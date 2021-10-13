package ru.ktsstudio.app_verification.presentation.object_survey.general

import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType

/**
 * Created by Igor Park on 13/01/2021.
 */
fun List<Reference>.withOtherOption(
    otherName: String = OTHER_REFERENCE
): List<Reference> {
    return this + listOf(
        Reference(
            id = OTHER_REFERENCE_ID,
            type = ReferenceType.OTHER,
            name = otherName
        )
    )
}

private const val OTHER_REFERENCE = "Иное"
private const val OTHER_REFERENCE_ID = "OTHER_REFERENCE_ID"
