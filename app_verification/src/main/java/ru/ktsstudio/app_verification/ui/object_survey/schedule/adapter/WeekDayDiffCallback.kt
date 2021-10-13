package ru.ktsstudio.app_verification.ui.object_survey.schedule.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.ktsstudio.app_verification.ui.object_survey.schedule.WeekDayUiItem

class WeekDayDiffCallback : DiffUtil.ItemCallback<WeekDayUiItem>() {
    override fun areItemsTheSame(oldItem: WeekDayUiItem, newItem: WeekDayUiItem): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: WeekDayUiItem, newItem: WeekDayUiItem): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: WeekDayUiItem, newItem: WeekDayUiItem): Any? {
        return Unit
    }
}
