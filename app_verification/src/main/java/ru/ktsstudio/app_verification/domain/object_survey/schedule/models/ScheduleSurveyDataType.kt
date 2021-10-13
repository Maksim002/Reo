package ru.ktsstudio.app_verification.domain.object_survey.schedule.models

import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.app_verification.ui.common.Updater

/**
 * @author Maxim Ovchinnikov on 02.12.2020.
 */
sealed class ScheduleSurveyDataType<T> : CheckableValueConsumer<T, ScheduleSurveyDraft> {

    data class ShiftsPerDayType(
        override val isChecked: Boolean,
        val value: String?
    ) : ScheduleSurveyDataType<String?>() {
        override fun get(): String? = value

        override fun consume(value: String?): Updater<ScheduleSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): Updater<ScheduleSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: ScheduleSurveyDraft): ScheduleSurveyDraft {
            return updatable.copy(
                scheduleSurvey = updatable.scheduleSurvey.copy(
                    shiftsPerDayCount = value?.toIntOrNull()
                ),
                scheduleCheckedSurvey = updatable.scheduleCheckedSurvey.copy(
                    shiftsPerDayCount = isChecked
                )
            )
        }
    }

    data class WorkdaysPerYearType(
        override val isChecked: Boolean,
        val value: String?
    ) : ScheduleSurveyDataType<String?>() {
        override fun get(): String? = value

        override fun consume(value: String?): Updater<ScheduleSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): Updater<ScheduleSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: ScheduleSurveyDraft): ScheduleSurveyDraft {
            return updatable.copy(
                scheduleSurvey = updatable.scheduleSurvey.copy(
                    workDaysPerYearCount = value?.toIntOrNull()
                ),
                scheduleCheckedSurvey = updatable.scheduleCheckedSurvey.copy(
                    workDaysPerYearCount = isChecked
                )
            )
        }
    }

    data class WorkplaceCountType(
        override val isChecked: Boolean,
        val value: String?
    ) : ScheduleSurveyDataType<String?>() {
        override fun get(): String? = value

        override fun consume(value: String?): Updater<ScheduleSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): Updater<ScheduleSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: ScheduleSurveyDraft): ScheduleSurveyDraft {
            return updatable.copy(
                scheduleSurvey = updatable.scheduleSurvey.copy(
                    workplacesCount = value?.toIntOrNull()
                ),
                scheduleCheckedSurvey = updatable.scheduleCheckedSurvey.copy(
                    workplacesCount = isChecked
                )
            )
        }
    }

    data class ManagersCountType(
        val value: String?,
        override val isChecked: Boolean
    ) : ScheduleSurveyDataType<String?>() {
        override fun get(): String? = value

        override fun consume(value: String?): Updater<ScheduleSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): Updater<ScheduleSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: ScheduleSurveyDraft): ScheduleSurveyDraft {
            return updatable.copy(
                scheduleSurvey = updatable.scheduleSurvey.copy(
                    managersCount = value?.toIntOrNull()
                ),
                scheduleCheckedSurvey = updatable.scheduleCheckedSurvey.copy(
                    managersCount = isChecked
                )
            )
        }
    }

    data class WorkersCountType(
        val value: String?,
        override val isChecked: Boolean
    ) : ScheduleSurveyDataType<String?>() {
        override fun get(): String? = value

        override fun consume(value: String?): Updater<ScheduleSurveyDraft> {
            return copy(value = value)
        }

        override fun setChecked(isChecked: Boolean): Updater<ScheduleSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: ScheduleSurveyDraft): ScheduleSurveyDraft {
            return updatable.copy(
                scheduleSurvey = updatable.scheduleSurvey.copy(
                    workersCount = value?.toIntOrNull()
                ),
                scheduleCheckedSurvey = updatable.scheduleCheckedSurvey.copy(
                    workersCount = isChecked
                )
            )
        }
    }
}
