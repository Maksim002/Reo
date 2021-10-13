package ru.ktsstudio.app_verification.ui.object_survey.common

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.ktsstudio.app_verification.presentation.object_survey.schedule.models.ScheduleTimeItem
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.DeletableReferenceAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.DeletableReferenceItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.DeleteEntityItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.DeleteEntityItemAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EditItemAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpaceAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerCheckAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerCheckItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerEditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerEditItemAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledComment
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledCommentAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledDateItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledDateItemAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItemAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSelector
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSelectorAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSurveyWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSurveyWithCheckAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLargeTitleAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerMediumTitle
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerMediumTitleAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledCommentWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledCommentWithCheckAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledEditDeletableItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledEditDeletableItemAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledEditItemWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledEditItemWithCheckAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledMultilineItemWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledMultilineItemWithCheckAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledSelectorWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledSelectorWithCheckAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LargeTitleItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LargeTitleItemAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.SubtitleItemWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.SubtitleItemWithCheckAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.media.LabeledMediaListItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.media.LabeledMediaListItemAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.schedule.WeekItemUi
import ru.ktsstudio.app_verification.ui.object_survey.schedule.adapter.ScheduleTimeAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.schedule.adapter.WeekAdapterDelegate
import ru.ktsstudio.common.ui.adapter.BaseListAdapter
import ru.ktsstudio.common.ui.adapter.delegates.AddEntityAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.AddEntityItem
import ru.ktsstudio.common.ui.adapter.delegates.CardBottomCornersAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardCornersItem
import ru.ktsstudio.common.ui.adapter.delegates.CardEmptyLine
import ru.ktsstudio.common.ui.adapter.delegates.CardEmptyLineAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardTopCornersAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.ListStateItem
import ru.ktsstudio.common.utils.isClassEqualTo
import ru.ktsstudio.core_data_verfication_api.data.model.Media

/**
 * @author Maxim Ovchinnikov on 20.02.2021.
 */
class SurveyAdapter<EntityType>(
    onRetry: () -> Unit,
    onDataChanged: (Updater<*>) -> Unit,
    onAddEntity: ((EntityType) -> Unit)? = null,
    onDeleteEntity: ((String, EntityType) -> Unit)? = null,
    onMediaAdd: ((ValueConsumer<List<Media>, *>) -> Unit)? = null,
    onMediaDelete: ((Pair<ValueConsumer<List<Media>, *>, Media>) -> Unit)? = null,
    timePickerRequested: ((String, (String) -> Updater<*>) -> Unit)? = null
) : BaseListAdapter(SurveyItemCallback()) {

    init {
        if (onMediaAdd != null && onMediaDelete != null) {
            delegatesManager.addDelegate(LabeledMediaListItemAdapterDelegate(onMediaAdd, onMediaDelete))
        }
        sequenceOf(
            WeekAdapterDelegate(onDataChanged),
            LargeTitleItemAdapterDelegate(),
            SubtitleItemWithCheckAdapterDelegate(onDataChanged),
            EmptySpaceAdapterDelegate(),
            CardEmptyLineAdapterDelegate(),
            CardBottomCornersAdapterDelegate(),
            CardTopCornersAdapterDelegate(),
            timePickerRequested?.let(::ScheduleTimeAdapterDelegate),
            onAddEntity?.let(::AddEntityAdapterDelegate),
            DeletableReferenceAdapterDelegate(onDataChanged),
            onDeleteEntity?.let(::DeleteEntityItemAdapterDelegate),
            onDeleteEntity?.let { LabeledEditDeletableItemAdapterDelegate(it, onDataChanged) },
            EditItemAdapterDelegate(onDataChanged),
            LabeledEditItemWithCheckAdapterDelegate(onDataChanged),
            LabeledCommentWithCheckAdapterDelegate(onDataChanged),
            LabeledSelectorWithCheckAdapterDelegate<Long>(onDataChanged),
            LabeledSelectorWithCheckAdapterDelegate<String>(onDataChanged),
            LabeledMultilineItemWithCheckAdapterDelegate(onDataChanged),
            InnerLargeTitleAdapterDelegate(),
            InnerLabeledEditItemAdapterDelegate(onDataChanged),
            InnerLabeledCommentAdapterDelegate(onDataChanged),
            InnerLabeledDateItemAdapterDelegate(onDataChanged),
            InnerCheckAdapterDelegate(onDataChanged),
            InnerEditItemAdapterDelegate(onDataChanged),
            InnerLabeledSurveyWithCheckAdapterDelegate(onDataChanged),
            InnerMediumTitleAdapterDelegate(),
            InnerLabeledSelectorAdapterDelegate<String>(onDataChanged)
        ).forEach { it?.let(delegatesManager::addDelegate) }
        delegatesManager.withDefaultDelegates(onRetry)
    }

    class SurveyItemCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (!oldItem.isClassEqualTo(newItem)) return false
            return when (oldItem) {
                is LargeTitleItem -> oldItem.text == (newItem as LargeTitleItem).text
                is SubtitleItemWithCheck -> oldItem.title == (newItem as SubtitleItemWithCheck).title
                is EmptySpace -> oldItem.isNested == (newItem as EmptySpace).isNested
                is CardEmptyLine -> oldItem.isNested == (newItem as CardEmptyLine).isNested
                is CardCornersItem -> oldItem.isTop == (newItem as CardCornersItem).isTop
                is ScheduleTimeItem -> oldItem.day == (newItem as ScheduleTimeItem).day
                is AddEntityItem<*> -> oldItem.text == (newItem as AddEntityItem<*>).text
                is DeleteEntityItem<*> -> oldItem.id == (newItem as DeleteEntityItem<*>).id
                is DeletableReferenceItem -> oldItem.id == (newItem as DeletableReferenceItem).id
                is EditItem -> oldItem.id == (newItem as EditItem).id
                is LabeledEditItemWithCheck -> oldItem.label == (newItem as LabeledEditItemWithCheck).label
                is LabeledEditDeletableItem<*> -> oldItem.id == (newItem as LabeledEditDeletableItem<*>).id
                is LabeledMultilineItemWithCheck -> oldItem.label == (newItem as LabeledMultilineItemWithCheck).label
                is LabeledSelectorWithCheck<*> -> oldItem.label == (newItem as LabeledSelectorWithCheck<*>).label
                is LabeledCommentWithCheck -> oldItem.label == (newItem as LabeledCommentWithCheck).label
                is LabeledMediaListItem -> oldItem.identifier == (newItem as LabeledMediaListItem).identifier
                is InnerEditItem -> oldItem.id == (newItem as InnerEditItem).id
                is InnerLabeledEditItem -> oldItem.id == (newItem as InnerLabeledEditItem).id
                is InnerMediumTitle -> oldItem.identifier == (newItem as InnerMediumTitle).identifier
                is InnerCheckItem -> oldItem.title == (newItem as InnerCheckItem).title
                is InnerLabeledComment -> oldItem.id == (newItem as InnerLabeledComment).id
                is InnerLabeledSelector<*> -> oldItem.identifier == (newItem as InnerLabeledSelector<*>).identifier
                is InnerLabeledDateItem -> oldItem.id == (newItem as InnerLabeledDateItem).id
                is InnerLabeledSurveyWithCheck -> oldItem.label == (newItem as InnerLabeledSurveyWithCheck).label
                is WeekItemUi -> true
                else -> false
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
            return when (newItem) {
                is ListStateItem,
                is SubtitleItemWithCheck,
                is ScheduleTimeItem,
                is EmptySpace,
                is CardEmptyLine,
                is CardCornersItem,
                is DeleteEntityItem<*>,
                is DeletableReferenceItem,
                is EditItem,
                is LabeledEditItemWithCheck,
                is LabeledEditDeletableItem<*>,
                is LabeledCommentWithCheck,
                is LabeledSelectorWithCheck<*>,
                is LabeledMultilineItemWithCheck,
                is LabeledMediaListItem,
                is InnerEditItem,
                is InnerCheckItem,
                is InnerLabeledEditItem,
                is InnerLabeledSelector<*>,
                is InnerLabeledComment,
                is InnerMediumTitle,
                is InnerLabeledDateItem,
                is InnerLabeledSurveyWithCheck -> newItem
                else -> null
            }
        }
    }
}
