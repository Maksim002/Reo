package ru.ktsstudio.core_data_verfication_api.data.model.schedule

import arrow.optics.optics
import ru.ktsstudio.common.utils.toggleItem

@optics
sealed class WorkMode {

    abstract fun changeToEveryDay(isFullDay: Boolean): WorkMode
    abstract fun changeToFullDay(): WorkMode
    abstract fun changeToWithoutBreaks(): WorkMode
    abstract fun getScheduleMap(): Map<WeekDay?, ScheduleTime>
    abstract fun getSelectedDays(): List<WeekDay>
    abstract fun changeSelectedDay(day: WeekDay): WorkMode
    abstract fun getWeekSchedule(): ScheduleTime?
    abstract fun checkValidity(): Boolean

    @optics
    data class EveryDay(
        val schedule: ScheduleTime
    ) : WorkMode() {
        override fun changeToFullDay(): WorkMode {
            return copy(schedule = schedule.copy(workTime = TimeRange.ALL_DAY))
        }

        override fun changeToWithoutBreaks(): WorkMode {
            return copy(schedule = schedule.copy(breakTime = TimeRange.EMPTY_RANGE))
        }

        override fun getScheduleMap(): Map<WeekDay?, ScheduleTime> {
            return mapOf(null to schedule)
        }

        override fun getSelectedDays(): List<WeekDay> {
            return WeekDay.values().toList()
        }

        override fun changeSelectedDay(day: WeekDay): WorkMode {
            return SelectedDays(
                workingDays = getSelectedDays().toggleItem(day),
                schedule = getDefaultScheduleMap()
            )
        }

        override fun getWeekSchedule(): ScheduleTime? {
            return schedule
        }

        override fun checkValidity(): Boolean {
            return true
        }

        override fun changeToEveryDay(isFullDay: Boolean): WorkMode {
            return SelectedDays(
                workingDays = getSelectedDays().toSet(),
                schedule = WeekDay.values().associateWith { schedule }
            )
        }

        companion object
    }

    @optics
    data class SelectedDays(
        val workingDays: Set<WeekDay>,
        val schedule: Map<WeekDay, ScheduleTime>
    ) : WorkMode() {
        override fun changeToFullDay(): WorkMode {
            return copy(
                schedule = schedule.mapValues { (_, time) ->
                    time.copy(workTime = TimeRange.ALL_DAY)
                }
            )
        }

        override fun changeToWithoutBreaks(): WorkMode {
            return copy(
                schedule = schedule.mapValues { (_, time) ->
                    time.copy(breakTime = TimeRange.EMPTY_RANGE)
                }
            )
        }

        override fun getScheduleMap(): Map<WeekDay?, ScheduleTime> {
            return schedule.filterKeys { getSelectedDays().contains(it) } as Map<WeekDay?, ScheduleTime>
        }

        override fun getSelectedDays(): List<WeekDay> {
            return workingDays.sortedBy { it.ordinal }
        }

        override fun changeSelectedDay(day: WeekDay): WorkMode {
            return copy(workingDays = workingDays.toggleItem(day))
        }

        override fun getWeekSchedule(): ScheduleTime? = null

        override fun checkValidity(): Boolean {
            return workingDays.isNotEmpty()
        }

        override fun changeToEveryDay(isFullDay: Boolean): WorkMode {
            return EveryDay(
                ScheduleTime(
                    workTime = if (isFullDay) TimeRange.ALL_DAY else TimeRange.EMPTY_RANGE,
                    breakTime = TimeRange.EMPTY_RANGE
                )
            )
        }

        companion object
    }

    fun changeWorkStartTime(day: WeekDay?, time: String): WorkMode {
        return setTime(day = day, time = time, work = true, start = true)
    }

    fun changeWorkEndTime(day: WeekDay?, time: String): WorkMode {
        return setTime(day = day, time = time, work = true, start = false)
    }

    fun changeBreakStartTime(day: WeekDay?, time: String): WorkMode {
        return setTime(day = day, time = time, work = false, start = true)
    }

    fun changeBreakEndTime(day: WeekDay?, time: String): WorkMode {
        return setTime(day = day, time = time, work = false, start = false)
    }

    private fun setTime(day: WeekDay?, time: String, start: Boolean, work: Boolean): WorkMode {
        return when (this) {
            is EveryDay -> {
                return when {
                    work && start -> {
                        EveryDay.schedule.workTime.from.set(this, time)
                    }
                    work && start.not() -> {
                        EveryDay.schedule.workTime.to.set(this, time)
                    }
                    work.not() && start -> {
                        EveryDay.schedule.breakTime.from.set(this, time)
                    }
                    work.not() && start.not() -> {
                        EveryDay.schedule.breakTime.to.set(this, time)
                    }
                    else -> error("Unreachable condition")
                }
            }
            is SelectedDays -> {
                if (day == null) return this
                fun updateTime(schedule: ScheduleTime, time: String): ScheduleTime {
                    return when {
                        work && start -> {
                            ScheduleTime.workTime.from.set(schedule, time)
                        }
                        work && start.not() -> {
                            ScheduleTime.workTime.to.set(schedule, time)
                        }
                        work.not() && start -> {
                            ScheduleTime.breakTime.from.set(schedule, time)
                        }
                        work.not() && start.not() -> {
                            ScheduleTime.breakTime.to.set(schedule, time)
                        }
                        else -> error("Unreachable condition")
                    }
                }

                SelectedDays.schedule.modify(this) {
                    it.toMutableMap()
                        .apply {
                            val schedule = this[day] ?: return@apply
                            this[day] = updateTime(schedule, time)
                        }
                }
            }
        }
    }

    protected fun getDefaultScheduleMap(): Map<WeekDay, ScheduleTime> {
        return WeekDay.values().associateWith { ScheduleTime.DEFAULT }
    }

    companion object
}
