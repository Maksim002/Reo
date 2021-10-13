package ru.ktsstudio.app_verification.ui.object_inspection

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.ktsstudio.app_verification.databinding.ItemProgressCardBinding
import ru.ktsstudio.app_verification.presentation.object_inspection.ProgressCardUi
import ru.ktsstudio.common.utils.inflate
import ru.ktsstudio.core_data_verfication_api.data.model.SurveySubtype

/**
 * Created by Igor Park on 04/12/2020.
 */
class ProgressCardAdapterDelegate(
    private val onCardClick: (SurveySubtype) -> Unit
) : AbsListItemAdapterDelegate<
    ProgressCardUi,
    Any,
    ProgressCardAdapterDelegate.ViewHolder
    >() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            binding = parent.inflate(ItemProgressCardBinding::inflate),
            onCardClick = onCardClick
        )
    }

    override fun isForViewType(
        item: Any,
        items: MutableList<Any>,
        position: Int
    ): Boolean {
        return item is ProgressCardUi
    }

    override fun onBindViewHolder(
        item: ProgressCardUi,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class ViewHolder(
        val binding: ItemProgressCardBinding,
        onCardClick: (SurveySubtype) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private var surveyType: SurveySubtype? = null

        init {
            itemView.setOnClickListener {
                surveyType?.let(onCardClick)
            }
        }

        fun bind(item: ProgressCardUi) = with(binding) {
            surveyType = item.surveySubtype
            progressTitle.setText(item.title)
            progressIcon.setImageResource(item.icon)
            progress.text = item.progressText
        }
    }
}
