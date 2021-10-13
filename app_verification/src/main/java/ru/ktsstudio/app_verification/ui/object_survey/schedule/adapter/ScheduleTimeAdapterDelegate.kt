package ru.ktsstudio.app_verification.ui.object_survey.schedule.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemDayScheduleBinding
import ru.ktsstudio.app_verification.presentation.object_survey.schedule.models.ScheduleTimeItem
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.utils.inflate

/**
 * Created by Igor Park on 09/02/2021.
 */
class ScheduleTimeAdapterDelegate(
    private val timePickerRequested: (String, (String) -> Updater<*>) -> Unit
) : AbsListItemAdapterDelegate<
    ScheduleTimeItem,
    Any,
    ScheduleTimeAdapterDelegate.Holder
    >() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            parent.inflate(ItemDayScheduleBinding::inflate),
            timePickerRequested
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is ScheduleTimeItem
    }

    override fun onBindViewHolder(item: ScheduleTimeItem, holder: Holder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    class Holder(
        val binding: ItemDayScheduleBinding,
        private val timePickerRequested: (String, (String) -> Updater<*>) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private var workStartUpdater: ValueConsumer<String?, *>? = null
        private var workEndUpdater: ValueConsumer<String?, *>? = null
        private var breakStartUpdater: ValueConsumer<String?, *>? = null
        private var breakEndUpdater: ValueConsumer<String?, *>? = null

        init {
            binding.root.isActivated = true
            binding.workStart.setOnClickListener { workStartUpdater?.let(::prepareTimeConsumeFunction) }
            binding.workEnd.setOnClickListener { workEndUpdater?.let(::prepareTimeConsumeFunction) }
            binding.breakStart.setOnClickListener { breakStartUpdater?.let(::prepareTimeConsumeFunction) }
            binding.breakEnd.setOnClickListener { breakEndUpdater?.let(::prepareTimeConsumeFunction) }
        }

        fun bind(item: ScheduleTimeItem) {
            workStartUpdater = item.workStart
            workEndUpdater = item.workEnd
            breakStartUpdater = item.breakStart
            breakEndUpdater = item.breakEnd

            with(binding) {
                dayTitle.text = item.displayName
                workStart.text = item.workStart.get()
                workEnd.text = item.workEnd.get()
                workStart.isEnabled = item.isFullDay.not()
                workEnd.isEnabled = item.isFullDay.not()

                breakStart.text = item.breakStart.get()
                breakEnd.text = item.breakEnd.get()
                breakStart.isEnabled = item.withoutBreaks.not()
                breakEnd.isEnabled = item.withoutBreaks.not()
            }
        }

        private fun prepareTimeConsumeFunction(valueConsumer: ValueConsumer<String?, *>) {
            val initialTime = valueConsumer.get().orEmpty()
            val timeConsumeFunc: (String) -> Updater<*> = { time -> valueConsumer.consume(time) }
            timePickerRequested.invoke(initialTime, timeConsumeFunc)
        }
    }
}
