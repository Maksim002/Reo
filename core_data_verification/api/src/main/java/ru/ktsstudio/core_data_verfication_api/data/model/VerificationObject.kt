package ru.ktsstudio.core_data_verfication_api.data.model

import arrow.optics.optics
import org.threeten.bp.Instant
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.ScheduleSurvey

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.11.2020.
 */
@optics
data class VerificationObject(
    val id: String,
    val gpsPoint: GpsPoint,
    val status: SurveyStatus?,
    val generalInformation: GeneralInformation,
    val date: Instant,
    val objectStatus: Reference?,
    val otherObjectStatusName: String?,
    val scheduleSurvey: ScheduleSurvey,
    val type: VerificationObjectType,
    val survey: Survey,
    val checkedSurvey: CheckedSurvey
) {
    companion object
}