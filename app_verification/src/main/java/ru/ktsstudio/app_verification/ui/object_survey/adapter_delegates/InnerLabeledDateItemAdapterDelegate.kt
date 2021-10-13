package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import ru.ktsstudio.app_verification.databinding.ItemInnerLabeledDateBinding
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.utils.inflate
import ru.ktsstudio.common.utils.openDateSelector
import ru.ktsstudio.common.utils.setupInCard

/**
 * @author Maxim Ovchinnikov on 15.01.2021.
 */
class InnerLabeledDateItemAdapterDelegate(
    private val onDataChanged: (Updater<*>) -> Unit
) : AbsListItemAdapterDelegate<InnerLabeledDateItem, Any, InnerLabeledDateItemAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            binding = parent.inflate(ItemInnerLabeledDateBinding::inflate),
            onDataChanged = onDataChanged
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is InnerLabeledDateItem
    }

    override fun onBindViewHolder(
        item: InnerLabeledDateItem,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            val valueConsumer = (payloads.first() as InnerLabeledDateItem)
                .valueConsumer
            holder.updateInput(valueConsumer)
        }
    }

    class Holder(
        private val binding: ItemInnerLabeledDateBinding,
        onDataChanged: (Updater<*>) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            .withZone(ZoneId.systemDefault())
        private var valueConsumer: ValueConsumer<Instant?, *>? = null

        init {
            binding.inputEditText.setOnClickListener {
                valueConsumer?.get()?.toEpochMilli().let { currentTime ->
                    itemView.context?.openDateSelector(
                        default = currentTime,
                        withTime = false
                    ) { updatedTime ->
                        valueConsumer?.consume(updatedTime.let(Instant::ofEpochMilli))
                            ?.let(onDataChanged)
                    }
                }
            }
        }

        fun bind(item: InnerLabeledDateItem) = with(binding) {
            root.isActivated = true
            container.setupInCard(item.inCard)
            labelTextView.text = item.label
            inputEditText.hint = item.editHint
            updateInput(item.valueConsumer)
        }

        fun updateInput(consumer: ValueConsumer<Instant?, *>) {
            valueConsumer = consumer
            val formattedDate = consumer.get()?.let(formatter::format) ?: ""
            binding.inputEditText.setText(formattedDate)
        }
    }
}
