package ru.ktsstudio.app_verification.ui.object_survey.schedule.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemWeekBinding
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.app_verification.ui.object_survey.schedule.WeekItemUi
import ru.ktsstudio.common.ui.SpreadingLayoutManager
import ru.ktsstudio.common.utils.inflate

/**
 * Created by Igor Park on 09/02/2021.
 */
class WeekAdapterDelegate(
    private val onDataChanged: (Updater<*>) -> Unit
) : AbsListItemAdapterDelegate<
    WeekItemUi,
    Any,
    WeekAdapterDelegate.Holder
    >() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            parent.inflate(ItemWeekBinding::inflate),
            onDataChanged
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is WeekItemUi
    }

    override fun onBindViewHolder(item: WeekItemUi, holder: Holder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    class Holder(
        val binding: ItemWeekBinding,
        onDataChanged: (Updater<*>) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private var valueConsumer: ValueConsumer<String?, *>? = null

        private val weekDayAdapter = WeekDayAdapter {
            valueConsumer?.consume(it.name)
                ?.let(onDataChanged)
        }

        init {
            binding.root.isActivated = true
            binding.weekDays.apply {
                adapter = weekDayAdapter
                layoutManager = SpreadingLayoutManager(
                    binding.root.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
        }

        fun bind(item: WeekItemUi) {
            valueConsumer = item.valueConsumer
            weekDayAdapter.submitList(item.workingDays)
        }
    }
}
