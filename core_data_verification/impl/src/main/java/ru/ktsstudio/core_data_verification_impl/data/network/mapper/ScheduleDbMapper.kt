package ru.ktsstudio.core_data_verification_impl.data.network.mapper

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.Schedule
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.WorkMode
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableDay
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializablePeriod
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableSchedule
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 23.11.2020.
 */
class ScheduleDbMapper @Inject constructor() : Mapper<Schedule, SerializableSchedule> {
    override fun map(item: Schedule): SerializableSchedule = with(item) {
        val isEveryDay = workMode is WorkMode.EveryDay
        val weekSchedule = workMode.getWeekSchedule()
        return SerializableSchedule(
            everyDay = isEveryDay,
            fullDay = isFullDay,
            withoutBreak = withoutBreaks,
            days = getRemoteDays(workMode),
            workTime = weekSchedule?.let {
                SerializablePeriod(from = it.workTime.from, to = it.workTime.to)
            }?.takeIf { isEveryDay },
            breakTime = weekSchedule?.let {
                SerializablePeriod(from = it.breakTime.from, to = it.breakTime.to)
            }?.takeIf { isEveryDay }
        )
    }

    private fun getRemoteDays(workMode: WorkMode): List<SerializableDay> {
        val selectedDays = workMode.getSelectedDays()
        return when (workMode) {
            is WorkMode.EveryDay -> {
                selectedDays.map {
                    SerializableDay(
                        isWorking = true,
                        workTime = null,
                        breakTime = null
                    )
                }
            }
            is WorkMode.SelectedDays -> {
                workMode.schedule.map { (day, time) ->
                    SerializableDay(
                        isWorking = selectedDays.contains(day),
                        workTime = SerializablePeriod(
                            from = time.workTime.from,
                            to = time.workTime.to
                        ),
                        breakTime = SerializablePeriod(
                            from = time.breakTime.from,
                            to = time.breakTime.to
                        )
                    )
                }
            }
        }
    }
}