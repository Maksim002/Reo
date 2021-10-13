package ru.ktsstudio.core_data_verification_impl.data.db.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.11.2020.
 */
data class LocalVerificationObjectWithRelation(
    @Embedded
    val verificationObject: LocalVerificationObject,
    @Relation(
        parentColumn = LocalVerificationObject.COLUMN_ID,
        entityColumn = LocalCheckedSurvey.COLUMN_OBJECT_ID
    )
    val checkedSurveyHolder: LocalCheckedSurvey
)