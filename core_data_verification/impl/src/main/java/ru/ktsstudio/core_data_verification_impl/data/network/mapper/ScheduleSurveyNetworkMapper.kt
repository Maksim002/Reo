package ru.ktsstudio.core_data_verification_impl.data.network.mapper

import com.google.gson.Gson
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.Schedule
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.ScheduleSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.ScheduleTime
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.TimeRange
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.WeekDay
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.WorkMode
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableObjectWorkSchedule
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableSchedule
import ru.ktsstudio.utilities.extensions.fromJson
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 23.11.2020.
 */
class ScheduleSurveyNetworkMapper @Inject constructor(
    private val gson: Gson
) : Mapper<SerializableObjectWorkSchedule, ScheduleSurvey> {
    override fun map(item: SerializableObjectWorkSchedule): ScheduleSurvey = with(item) {
        val serializableSchedule = scheduleJson?.let {
            gson.fromJson<SerializableSchedule>(it)
        }
        return ScheduleSurvey(
            schedule = serializableSchedule?.let {
                Schedule(
                    isFullDay = serializableSchedule.fullDay,
                    withoutBreaks = serializableSchedule.withoutBreak,
                    workMode = mapWorkMode(serializableSchedule)
                )
            },
            shiftsPerDayCount = shiftsPerDayCount,
            workDaysPerYearCount = daysPerYearCount,
            workplacesCount = workplacesCount,
            managersCount = managersCount,
            workersCount = workersCount
        )
    }

    private fun mapWorkMode(remoteWorkMode: SerializableSchedule): WorkMode {
        return if (remoteWorkMode.everyDay) {
            mapEveryDay(remoteWorkMode)
        } else {
            mapSelectedDays(remoteWorkMode)
        }
    }

    private fun mapEveryDay(remoteWorkMode: SerializableSchedule): WorkMode.EveryDay {
        return WorkMode.EveryDay(
            schedule = ScheduleTime(
                workTime = remoteWorkMode.workTime?.let {
                    TimeRange(from = it.from, to = it.to)
                } ?: TimeRange.EMPTY_RANGE,
                breakTime = remoteWorkMode.breakTime?.let {
                    TimeRange(from = it.from, to = it.to)
                } ?: TimeRange.EMPTY_RANGE
            )
        )
    }

    private fun mapSelectedDays(remoteWorkMode: SerializableSchedule): WorkMode.SelectedDays {
        val days = WeekDay.values().toList()
        fun mapRemoteDayIndex(index: Int): WeekDay = days[index]

        val selectedDays = mutableSetOf<WeekDay>()
        val scheduleMap = mutableMapOf<WeekDay, ScheduleTime>()
        remoteWorkMode.days.orEmpty().forEachIndexed { index, remoteDay ->
            val day = mapRemoteDayIndex(index)
            if (remoteDay.isWorking) selectedDays.add(day)
            scheduleMap[day] = ScheduleTime(
                workTime = remoteDay.workTime?.let {
                    TimeRange(from = it.from, to = it.to)
                } ?: TimeRange.EMPTY_RANGE,
                breakTime = remoteDay.workTime?.let {
                    TimeRange(from = it.from, to = it.to)
                } ?: TimeRange.EMPTY_RANGE
            )

        }
        return WorkMode.SelectedDays(
            workingDays = selectedDays,
            schedule = scheduleMap
        )
    }
}