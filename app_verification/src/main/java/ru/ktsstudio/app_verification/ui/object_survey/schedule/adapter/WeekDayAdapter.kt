package ru.ktsstudio.app_verification.ui.object_survey.schedule.adapter

import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.databinding.ItemWeekDayBinding
import ru.ktsstudio.app_verification.ui.object_survey.schedule.WeekDayUiItem
import ru.ktsstudio.common.utils.inflate
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.WeekDay

/**
 * Created by Igor Park on 09/02/2021.
 */
class WeekDayAdapter(
    private val onDaySelected: (WeekDay) -> Unit
) : ListAdapter<WeekDayUiItem, WeekDayAdapter.Holder>(WeekDayDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            parent.inflate(ItemWeekDayBinding::inflate),
            onDaySelected
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class Holder(
        private val binding: ItemWeekDayBinding,
        onDaySelected: (WeekDay) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private var selectedDay: WeekDay? = null

        init {
            itemView.setOnClickListener {
                selectedDay?.let(onDaySelected)
            }
        }

        fun bind(weekDay: WeekDayUiItem) = with(binding) {
            selectedDay = weekDay.day
            root.setBackgroundResource(getBackgroundRes(weekDay.isFirst, weekDay.isLast))
            title.text = weekDay.title
            root.isActivated = weekDay.isSelected
            title.isActivated = weekDay.isSelected
        }

        @DrawableRes
        private fun getBackgroundRes(isFirst: Boolean, isLast: Boolean): Int {
            return when {
                isFirst -> R.drawable.bg_week_day_left_corners
                isLast -> R.drawable.bg_week_day_right_corners
                else -> R.drawable.bg_week_day_middle
            }
        }
    }
}
