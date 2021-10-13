package ru.ktsstudio.core_data_verification_impl.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.ktsstudio.core_data_verfication_api.data.model.CheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.db.converter.CheckedSurveyConverter

/**
 * @author Maxim Ovchinnikov on 26.11.2020.
 */
@Entity(
    tableName = LocalCheckedSurvey.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = LocalVerificationObject::class,
            parentColumns = [LocalVerificationObject.COLUMN_ID],
            childColumns = [LocalCheckedSurvey.COLUMN_OBJECT_ID],
            deferred = true
        )
    ]
)
@TypeConverters(CheckedSurveyConverter::class)
data class LocalCheckedSurvey(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_OBJECT_ID)
    val objectId: String,
    @ColumnInfo(name = COLUMN_CHECKED_SURVEY)
    val checkedSurvey: CheckedSurvey
) {

    companion object {
        const val TABLE_NAME = "checked_survey"

        const val COLUMN_OBJECT_ID = "object_id"
        private const val COLUMN_CHECKED_SURVEY = "checked_survey"
    }
}