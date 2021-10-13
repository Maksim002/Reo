package ru.ktsstudio.core_data_verification_impl.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.threeten.bp.Instant
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.data.models.LocalModelState
import ru.ktsstudio.core_data_verfication_api.data.model.GeneralInformation
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.ScheduleSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.Survey
import ru.ktsstudio.core_data_verfication_api.data.model.SurveyStatus
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verification_impl.data.db.converter.GeneralInformationConverter
import ru.ktsstudio.core_data_verification_impl.data.db.converter.ReferenceConverter
import ru.ktsstudio.core_data_verification_impl.data.db.converter.ScheduleConverter
import ru.ktsstudio.core_data_verification_impl.data.db.converter.SurveyConverter
import ru.ktsstudio.core_data_verification_impl.data.db.converter.SurveyStatusConverter

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.11.2020.
 */
@Entity(
    tableName = LocalVerificationObject.TABLE_NAME
)
@TypeConverters(
    ScheduleConverter::class,
    SurveyConverter::class,
    SurveyStatusConverter::class,
    GeneralInformationConverter::class,
    ReferenceConverter::class
)
data class LocalVerificationObject(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
    @ColumnInfo(name = COLUMN_LOCATION)
    val location: GpsPoint,
    @ColumnInfo(name = COLUMN_ADDRESS)
    val address: String,
    @ColumnInfo(name = COLUMN_OBJECT_STATUS)
    val objectStatus: Reference?,
    @ColumnInfo(name = COLUMN_OTHER_OBJECT_STATUS_NAME)
    val otherObjectStatusName: String?,
    @ColumnInfo(name = COLUMN_DATE)
    val date: Instant,
    @ColumnInfo(name = COLUMN_SCHEDULE)
    val scheduleSurvey: ScheduleSurvey,
    @ColumnInfo(name = COLUMN_GENERAL_INFO)
    val generalInformation: GeneralInformation,
    @ColumnInfo(name = COLUMN_TYPE)
    val type: VerificationObjectType,
    @ColumnInfo(name = COLUMN_SURVEY)
    val survey: Survey,
    @ColumnInfo(name = COLUMN_LOCAL_STATE)
    val state: LocalModelState,
    @ColumnInfo(name = COLUMN_SURVEY_STATUS)
    val status: SurveyStatus?
) {
    companion object {
        const val TABLE_NAME = "verification_object"

        const val COLUMN_ID = "id"
        private const val COLUMN_LOCATION = "location"
        private const val COLUMN_ADDRESS = "address"
        const val COLUMN_SURVEY_STATUS = "survey_status"
        const val COLUMN_OBJECT_STATUS = "object_status"
        const val COLUMN_OTHER_OBJECT_STATUS_NAME = "other_object_status"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_SCHEDULE = "schedule"
        private const val COLUMN_GENERAL_INFO = "generalInfo"
        private const val COLUMN_TYPE = "type"
        private const val COLUMN_SURVEY = "survey"
        const val COLUMN_LOCAL_STATE = "localState"
    }
}