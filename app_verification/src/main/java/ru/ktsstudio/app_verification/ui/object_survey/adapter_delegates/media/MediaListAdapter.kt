package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.media

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import ru.ktsstudio.app_verification.presentation.object_survey.common.SurveyMediaUi
import ru.ktsstudio.common.utils.isClassEqualTo
import ru.ktsstudio.core_data_verfication_api.data.model.Media

internal class MediaListAdapter(
    onDeleteClick: (Media) -> Unit,
    onAddClick: () -> Unit
) : AsyncListDifferDelegationAdapter<Any>(MediaListItemDiffCallback()) {

    init {
        sequenceOf(
            MediaItemDelegate(onDeleteClick),
            AddMediaDelegate(onAddClick)
        ).forEach { delegatesManager.addDelegate(it) }
    }

    private class MediaListItemDiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem.isClassEqualTo(newItem).not()) return false
            return when (oldItem) {
                is SurveyMediaUi.AddItem -> {
                    oldItem.identifier == (newItem as SurveyMediaUi.AddItem).identifier
                }
                is SurveyMediaUi.LoadingMedia -> {
                    oldItem.media.id == (newItem as SurveyMediaUi.LoadingMedia).media.id
                }
                else -> true
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem == newItem

        override fun getChangePayload(oldItem: Any, newItem: Any): Any = Unit
    }
}
