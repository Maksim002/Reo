package ru.ktsstudio.core_data_verification_impl.data.db.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.Schedule
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.ScheduleSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableObjectWorkSchedule
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableSchedule
import ru.ktsstudio.utilities.extensions.fromJson
import ru.ktsstudio.utilities.extensions.requireNotNull
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 23.11.2020.
 */
@ProvidedTypeConverter
class ScheduleConverter @Inject constructor(
    private val gson: Gson,
    private val scheduleSurveyMapper: Mapper<SerializableObjectWorkSchedule, ScheduleSurvey>,
    private val scheduleDbMapper: Mapper<Schedule, SerializableSchedule>
) {
    @TypeConverter
    fun convertToString(scheduleSurvey: ScheduleSurvey): String {
        return scheduleSurvey.let {
            SerializableObjectWorkSchedule(
                scheduleJson = scheduleSurvey.schedule?.let(scheduleDbMapper::map)
                    ?.let(gson::toJson),
                shiftsPerDayCount = it.shiftsPerDayCount,
                daysPerYearCount = it.workDaysPerYearCount,
                workplacesCount = it.workplacesCount,
                managersCount = it.managersCount,
                workersCount = it.workersCount
            )
        }.let { gson.toJson(it) }
    }

    @TypeConverter
    fun convertFromString(scheduleString: String): ScheduleSurvey {
        return gson.fromJson<SerializableObjectWorkSchedule>(scheduleString)
            .requireNotNull()
            .let(scheduleSurveyMapper::map)
    }
}