package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.databinding.ItemSubtitleWithCheckBinding
import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.setParagraphSmallBold
import ru.ktsstudio.common.utils.inflate
import ru.ktsstudio.common.utils.setCheckedWithoutEventTrigger
import ru.ktsstudio.utilities.extensions.orDefault
import ru.ktsstudio.utilities.extensions.updatePadding

/**
 * @author Maxim Ovchinnikov on 27.11.2020.
 */
class SubtitleItemWithCheckAdapterDelegate(
    private val onDataChanged: (Updater<*>) -> Unit
) : AbsListItemAdapterDelegate<
    SubtitleItemWithCheck,
    Any,
    SubtitleItemWithCheckAdapterDelegate.Holder
    >() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
            binding = parent.inflate(ItemSubtitleWithCheckBinding::inflate),
            onDataChanged = onDataChanged
        )
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is SubtitleItemWithCheck
    }

    override fun onBindViewHolder(
        item: SubtitleItemWithCheck,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            payloads.first()
                .let { it as SubtitleItemWithCheck }
                .also { holder.bind(it) }
        }
    }

    class Holder(
        private val binding: ItemSubtitleWithCheckBinding,
        onDataChanged: (Updater<*>) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var checkableValueConsumer: CheckableValueConsumer<Unit, *>? = null
        private val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)
        private val checkChangeListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            checkableValueConsumer?.setChecked(isChecked)
                ?.let(onDataChanged)
        }

        init {
            itemView.setOnClickListener { checkBox.toggle() }
            checkBox.setOnCheckedChangeListener(checkChangeListener)
        }

        fun bind(item: SubtitleItemWithCheck) {
            itemView.isActivated = item.isNested
            checkableValueConsumer = item.checkableValueConsumer
            checkBox.setCheckedWithoutEventTrigger(item.checkableValueConsumer.isChecked, checkChangeListener)
            binding.titleTextView.text = item.title
            binding.titleTextView.setParagraphSmallBold(item.withAccent)
            val bottomPadding = item.bottomPadding
                ?.let(binding.root.context.resources::getDimensionPixelSize)
                .orDefault(0)
            binding.root.updatePadding(bottom = bottomPadding)
        }
    }
}
