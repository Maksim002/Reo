package ru.ktsstudio.app_verification.domain.object_survey.general.models

import arrow.optics.optics
import ru.ktsstudio.core_data_verfication_api.data.model.GeneralCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.GeneralInformation
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

/**
 * Created by Igor Park on 04/12/2020.
 */
@optics
data class GeneralSurveyDraft(
    val objectStatus: Reference?,
    val information: GeneralInformation,
    val generalCheckedSurvey: GeneralCheckedSurvey,
    val hasOtherObjectStatus: Boolean,
    val otherStatusName: String?,
    val objectStatuses: List<Reference>,
    val subjects: List<Reference>,
    val type: VerificationObjectType
) {
    companion object
}
