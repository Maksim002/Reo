package ru.ktsstudio.app_verification.presentation.object_survey.schedule

import org.threeten.bp.DayOfWeek
import org.threeten.bp.format.DateTimeFormatterBuilder
import org.threeten.bp.format.TextStyle
import org.threeten.bp.temporal.ChronoField
import org.threeten.bp.temporal.TemporalAccessor
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.BooleanValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.schedule.models.ScheduleSurveyDataType
import ru.ktsstudio.app_verification.domain.object_survey.schedule.models.ScheduleSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.schedule.models.scheduleSurvey
import ru.ktsstudio.app_verification.presentation.object_survey.schedule.models.ScheduleTimeItem
import ru.ktsstudio.app_verification.presentation.object_survey.tech.CheckableTitle
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerCheckItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledEditItemWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LargeTitleItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.SubtitleItemWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.schedule.WeekDayUiItem
import ru.ktsstudio.app_verification.ui.object_survey.schedule.WeekItemUi
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.TimeRange
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.WeekDay
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.WorkMode
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.isFullDay
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.schedule
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.withoutBreaks
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.workMode
import ru.ktsstudio.utilities.extensions.orFalse
import java.util.Locale

/**
 * @author Maxim Ovchinnikov on 27.11.2020.
 */
internal class ObjectScheduleSurveyUiStateTransformer(
    private val resources: ResourceManager
) : (ObjectSurveyFeature.State<ScheduleSurveyDraft>) -> ObjectScheduleSurveyUiState {

    override fun invoke(state: ObjectSurveyFeature.State<ScheduleSurveyDraft>): ObjectScheduleSurveyUiState {
        return ObjectScheduleSurveyUiState(
            loading = state.loading,
            error = state.error,
            data = state.draft?.let(::createAdapterItems) ?: emptyList()
        )
    }

    private fun createAdapterItems(surveyDraft: ScheduleSurveyDraft): List<Any> {
        return listOf(LargeTitleItem(resources.getString(R.string.survey_schedule_title))) +
            updateWorkModeUI(surveyDraft) +
            listOf(
                LabeledEditItemWithCheck(
                    label = resources.getString(R.string.survey_schedule_shift_per_day),
                    checkableValueConsumer = ScheduleSurveyDataType.ShiftsPerDayType(
                        isChecked = surveyDraft.scheduleCheckedSurvey.shiftsPerDayCount,
                        value = surveyDraft.scheduleSurvey.shiftsPerDayCount?.toString()
                    ),
                    editHint = resources.getString(R.string.survey_number_hint),
                    inputFormat = TextFormat.Number()
                ),
                LabeledEditItemWithCheck(
                    label = resources.getString(R.string.survey_schedule_workdays_per_year),
                    checkableValueConsumer = ScheduleSurveyDataType.WorkdaysPerYearType(
                        isChecked = surveyDraft.scheduleCheckedSurvey.workDaysPerYearCount,
                        value = surveyDraft.scheduleSurvey.workDaysPerYearCount?.toString()
                    ),
                    editHint = resources.getString(R.string.survey_number_hint),
                    inputFormat = TextFormat.Number(max = 365F)
                ),
                LabeledEditItemWithCheck(
                    label = resources.getString(R.string.survey_schedule_workplace_count),
                    checkableValueConsumer = ScheduleSurveyDataType.WorkplaceCountType(
                        isChecked = surveyDraft.scheduleCheckedSurvey.workplacesCount,
                        value = surveyDraft.scheduleSurvey.workplacesCount?.toString()
                    ),
                    editHint = resources.getString(R.string.survey_number_hint),
                    inputFormat = TextFormat.Number()
                ),
                LabeledEditItemWithCheck(
                    label = resources.getString(R.string.survey_schedule_management_personnel_count),
                    checkableValueConsumer = ScheduleSurveyDataType.ManagersCountType(
                        isChecked = surveyDraft.scheduleCheckedSurvey.managersCount,
                        value = surveyDraft.scheduleSurvey.managersCount?.toString()
                    ),
                    editHint = resources.getString(R.string.survey_number_hint),
                    inputFormat = TextFormat.Number()
                ),
                LabeledEditItemWithCheck(
                    label = resources.getString(R.string.survey_schedule_production_personnel_count),
                    checkableValueConsumer = ScheduleSurveyDataType.WorkersCountType(
                        isChecked = surveyDraft.scheduleCheckedSurvey.workersCount,
                        value = surveyDraft.scheduleSurvey.workersCount?.toString()
                    ),
                    editHint = resources.getString(R.string.survey_number_hint),
                    inputFormat = TextFormat.Number()
                )
            )
    }

    private fun updateWorkModeUI(surveyDraft: ScheduleSurveyDraft): List<Any> {
        val workModeInstance = surveyDraft.scheduleSurvey.schedule?.workMode
        val selectedDays = workModeInstance?.getSelectedDays().orEmpty()
        val workModeOptics = ScheduleSurveyDraft.scheduleSurvey.schedule.workMode
        val isFullDay = surveyDraft.scheduleSurvey.schedule?.isFullDay.orFalse()
        val withoutBreaks = surveyDraft.scheduleSurvey.schedule?.withoutBreaks.orFalse()

        return listOf(
            SubtitleItemWithCheck(
                title = resources.getString(R.string.survey_week_schedule_title),
                isNested = true,
                withAccent = true,
                bottomPadding = null,
                checkableValueConsumer = CheckableTitle<ScheduleSurveyDraft>(
                    isChecked = surveyDraft.scheduleCheckedSurvey.schedule,
                    updater = { isChecked, draft ->
                        draft.copy(
                            scheduleCheckedSurvey = draft.scheduleCheckedSurvey.copy(
                                schedule = isChecked
                            )
                        )
                    }
                )
            ),
            InnerCheckItem(
                title = resources.getString(R.string.survey_schedule_every_day_title),
                valueConsumer = BooleanValueConsumer<ScheduleSurveyDraft>(
                    value = surveyDraft.scheduleSurvey.schedule?.workMode is WorkMode.EveryDay,
                    updater = { _, draft ->
                        workModeOptics.modify(draft) { workMode ->
                            workMode.changeToEveryDay(
                                isFullDay = draft.scheduleSurvey
                                    .schedule
                                    ?.isFullDay
                                    .orFalse()
                            )
                        }
                    }
                )
            ),
            InnerCheckItem(
                title = resources.getString(R.string.survey_schedule_full_day_title),
                valueConsumer = BooleanValueConsumer<ScheduleSurveyDraft>(
                    value = surveyDraft.scheduleSurvey.schedule?.isFullDay.orFalse(),
                    updater = { isChecked, draft ->
                        val updated = ScheduleSurveyDraft.scheduleSurvey
                            .schedule
                            .isFullDay
                            .set(draft, isChecked.orFalse())
                        workModeOptics.modify(updated) { workMode -> workMode.changeToFullDay() }
                    }
                )
            ),
            InnerCheckItem(
                title = resources.getString(R.string.survey_schedule_without_breaks_title),
                valueConsumer = BooleanValueConsumer<ScheduleSurveyDraft>(
                    value = surveyDraft.scheduleSurvey.schedule?.withoutBreaks.orFalse(),
                    updater = { isChecked, draft ->
                        val updated = ScheduleSurveyDraft.scheduleSurvey
                            .schedule
                            .withoutBreaks
                            .set(draft, isChecked.orFalse())
                        workModeOptics.modify(updated) { workMode -> workMode.changeToWithoutBreaks() }
                    }
                )
            ),
            WeekItemUi(
                workingDays = WeekDay.values()
                    .mapIndexed { index, weekDay ->
                        WeekDayUiItem(
                            day = weekDay,
                            title = weekDay.getShortDisplayName(),
                            isFirst = index == 0,
                            isLast = index == WeekDay.values().lastIndex,
                            isSelected = selectedDays.contains(weekDay)
                        )
                    },
                valueConsumer = StringValueConsumer(null) { name, draft ->
                    ScheduleSurveyDraft.scheduleSurvey
                        .schedule
                        .workMode
                        .modify(draft) { workMode ->
                            name?.let { workMode.changeSelectedDay(WeekDay.valueOf(name)) }
                                ?: workMode
                        }
                }
            )
        ) + workModeInstance?.getScheduleMap()
            .orEmpty()
            .map { (day, scheduleTime) ->
                ScheduleTimeItem(
                    day = day,
                    displayName = day.getFullDisplayName(
                        default = resources.getString(R.string.survey_schedule_every_day_title)
                    ),
                    isFullDay = isFullDay,
                    withoutBreaks = withoutBreaks,
                    workStart = StringValueConsumer(scheduleTime.workTime.from) { time, draft ->
                        workModeOptics.modify(draft) { workMode ->
                            workMode.changeWorkStartTime(day, time ?: TimeRange.EMPTY_TIME)
                        }
                    },
                    workEnd = StringValueConsumer(scheduleTime.workTime.to) { time, draft ->
                        workModeOptics.modify(draft) { workMode ->
                            workMode.changeWorkEndTime(day, time ?: TimeRange.EMPTY_TIME)
                        }
                    },
                    breakStart = StringValueConsumer(scheduleTime.breakTime.from) { time, draft ->
                        workModeOptics.modify(draft) { workMode ->
                            workMode.changeBreakStartTime(day, time ?: TimeRange.EMPTY_TIME)
                        }
                    },
                    breakEnd = StringValueConsumer(scheduleTime.breakTime.to) { time, draft ->
                        workModeOptics.modify(draft) { workMode ->
                            workMode.changeBreakEndTime(day, time ?: TimeRange.EMPTY_TIME)
                        }
                    }
                )
            }
    }

    companion object {
        private fun WeekDay?.getFullDisplayName(default: String): String {
            return this?.mapToTemporalAccessor()
                ?.let(fullWeekDayFormatter::format)
                ?.capitalize(Locale("ru"))
                ?: default
        }

        private fun WeekDay.getShortDisplayName(): String {
            return mapToTemporalAccessor()
                .let(shortWeekDayFormatter::format)
                .capitalize(Locale("ru"))
        }

        private val fullWeekDayFormatter = DateTimeFormatterBuilder()
            .appendText(ChronoField.DAY_OF_WEEK, TextStyle.FULL)
            .toFormatter(Locale("ru"))

        private val shortWeekDayFormatter = DateTimeFormatterBuilder()
            .appendText(ChronoField.DAY_OF_WEEK, TextStyle.SHORT)
            .toFormatter(Locale("ru"))

        private fun WeekDay.mapToTemporalAccessor(): TemporalAccessor {
            return when (this) {
                WeekDay.MONDAY -> DayOfWeek.MONDAY
                WeekDay.TUESDAY -> DayOfWeek.TUESDAY
                WeekDay.WEDNESDAY -> DayOfWeek.WEDNESDAY
                WeekDay.THURSDAY -> DayOfWeek.THURSDAY
                WeekDay.FRIDAY -> DayOfWeek.FRIDAY
                WeekDay.SATURDAY -> DayOfWeek.SATURDAY
                WeekDay.SUNDAY -> DayOfWeek.SUNDAY
            }
        }
    }
}
