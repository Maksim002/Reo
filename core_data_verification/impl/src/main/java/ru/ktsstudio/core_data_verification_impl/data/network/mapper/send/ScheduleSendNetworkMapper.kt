package ru.ktsstudio.core_data_verification_impl.data.network.mapper.send

import com.google.gson.Gson
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.Schedule
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.ScheduleSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableObjectWorkSchedule
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableSchedule
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 20.12.2020.
 */
class ScheduleSendNetworkMapper @Inject constructor(
    private val gson: Gson,
    private val scheduleDbMapper: Mapper<Schedule, SerializableSchedule>
) : Mapper<ScheduleSurvey, SerializableObjectWorkSchedule> {

    override fun map(item: ScheduleSurvey): SerializableObjectWorkSchedule = with(item) {
        return SerializableObjectWorkSchedule(
            scheduleJson = schedule?.let(scheduleDbMapper::map)
                ?.let(gson::toJson),
            shiftsPerDayCount = shiftsPerDayCount,
            daysPerYearCount = workDaysPerYearCount,
            workplacesCount = workplacesCount,
            managersCount = managersCount,
            workersCount = workersCount
        )
    }
}