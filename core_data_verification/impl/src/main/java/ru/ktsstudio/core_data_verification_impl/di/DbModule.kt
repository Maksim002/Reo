package ru.ktsstudio.core_data_verification_impl.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.ktsstudio.common.data.db.DatabaseCleaner
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.core_data_verification_impl.data.db.VerificationDb
import ru.ktsstudio.core_data_verification_impl.data.db.cleaner.DatabaseCleanerImpl
import ru.ktsstudio.core_data_verification_impl.data.db.converter.CheckedSurveyConverter
import ru.ktsstudio.core_data_verification_impl.data.db.converter.EnumConverters
import ru.ktsstudio.core_data_verification_impl.data.db.converter.GeneralInformationConverter
import ru.ktsstudio.core_data_verification_impl.data.db.converter.ReferenceConverter
import ru.ktsstudio.core_data_verification_impl.data.db.converter.ScheduleConverter
import ru.ktsstudio.core_data_verification_impl.data.db.converter.SurveyConverter
import ru.ktsstudio.core_data_verification_impl.data.db.converter.SurveyStatusConverter
import ru.ktsstudio.core_data_verification_impl.data.db.dao.CheckedSurveyDao
import ru.ktsstudio.core_data_verification_impl.data.db.dao.MediaDao
import ru.ktsstudio.core_data_verification_impl.data.db.dao.ReferenceDao
import ru.ktsstudio.core_data_verification_impl.data.db.dao.VerificationObjectDao

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.11.2020.
 */
@Module
interface DbModule {

    @Binds
    fun bindDatabaseCleaner(impl: DatabaseCleanerImpl): DatabaseCleaner

    companion object {

        @Provides
        @FeatureScope
        fun provideDb(
            context: Context,
            generalInformationConverter: GeneralInformationConverter,
            referenceConverter: ReferenceConverter,
            scheduleConverter: ScheduleConverter,
            surveyConverter: SurveyConverter,
            checkedSurveyConverter: CheckedSurveyConverter,
            enumConverters: EnumConverters,
            statusConverter: SurveyStatusConverter
        ): VerificationDb {
            return Room.databaseBuilder(
                context,
                VerificationDb::class.java,
                VerificationDb.dbName
            )
                .addTypeConverter(generalInformationConverter)
                .addTypeConverter(referenceConverter)
                .addTypeConverter(scheduleConverter)
                .addTypeConverter(surveyConverter)
                .addTypeConverter(checkedSurveyConverter)
                .addTypeConverter(enumConverters)
                .addTypeConverter(statusConverter)
                .build()
        }

        @Provides
        fun provideReferenceDao(db: VerificationDb): ReferenceDao = db.referenceDao()

        @Provides
        fun provideVerificationObjectDao(db: VerificationDb): VerificationObjectDao =
            db.verificationObjectDao()

        @Provides
        fun provideCheckedSurveyDao(db: VerificationDb): CheckedSurveyDao =
            db.checkedSurveyDao()

        @Provides
        fun provideMediaDao(db: VerificationDb): MediaDao =
            db.mediaDao()
    }
}