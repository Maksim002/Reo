package ru.ktsstudio.app_verification.domain.object_survey.tech.models

import arrow.optics.optics
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalSurvey

/**
 * @author Maxim Ovchinnikov on 02.12.2020.
 */
@optics
data class TechnicalSurveyDraft(
    val technicalSurvey: TechnicalSurvey,
    val technicalCheckedSurvey: TechnicalCheckedSurvey
) {
    companion object
}
