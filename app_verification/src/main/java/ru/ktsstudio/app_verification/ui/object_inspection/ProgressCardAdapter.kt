package ru.ktsstudio.app_verification.ui.object_inspection

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.ktsstudio.app_verification.presentation.object_inspection.ProgressCardUi
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LargeTitleItemAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LargeTitleItem
import ru.ktsstudio.common.ui.adapter.BaseListAdapter
import ru.ktsstudio.common.utils.isClassEqualTo
import ru.ktsstudio.core_data_verfication_api.data.model.SurveySubtype

/**
 * Created by Igor Park on 04/12/2020.
 */
class ProgressCardAdapter(
    onCardClick: (SurveySubtype) -> Unit,
    onRetry: () -> Unit
) : BaseListAdapter(WasteTypeFieldDiffCallback()) {

    init {
        sequenceOf(
            LargeTitleItemAdapterDelegate(),
            ProgressCardAdapterDelegate(onCardClick)
        ).forEach { delegatesManager.addDelegate(it) }
        delegatesManager.withDefaultDelegates(onRetry)
    }

    private class WasteTypeFieldDiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem.isClassEqualTo(newItem).not()) return false
            return when (oldItem) {
                is LargeTitleItem -> (newItem as LargeTitleItem).text == oldItem.text
                is ProgressCardUi -> (newItem as ProgressCardUi).surveySubtype == oldItem.surveySubtype
                else -> false
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
            return when (oldItem) {
                is ProgressCardUi -> newItem
                else -> null
            }
        }
    }
}
