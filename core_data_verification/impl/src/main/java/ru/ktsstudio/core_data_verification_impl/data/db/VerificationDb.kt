package ru.ktsstudio.core_data_verification_impl.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.ktsstudio.common.utils.db.converters.GpsPointRoomConverter
import ru.ktsstudio.common.utils.db.converters.InstantDateConverter
import ru.ktsstudio.core_data_verification_impl.data.db.converter.EnumConverters
import ru.ktsstudio.core_data_verification_impl.data.db.dao.CheckedSurveyDao
import ru.ktsstudio.core_data_verification_impl.data.db.dao.MediaDao
import ru.ktsstudio.core_data_verification_impl.data.db.dao.ReferenceDao
import ru.ktsstudio.core_data_verification_impl.data.db.dao.VerificationObjectDao
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalReference
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalVerificationObject

/**
 * @author Maxim Ovchinnikov on 10.11.2020.
 */
@Database(
    entities = [
        LocalVerificationObject::class,
        LocalCheckedSurvey::class,
        LocalReference::class,
        LocalMedia::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    EnumConverters::class,
    InstantDateConverter::class,
    GpsPointRoomConverter::class
)
abstract class VerificationDb : RoomDatabase() {

    abstract fun verificationObjectDao(): VerificationObjectDao
    abstract fun checkedSurveyDao(): CheckedSurveyDao
    abstract fun referenceDao(): ReferenceDao
    abstract fun mediaDao(): MediaDao

    companion object {
        const val dbName = "VerificationDb"
    }
}