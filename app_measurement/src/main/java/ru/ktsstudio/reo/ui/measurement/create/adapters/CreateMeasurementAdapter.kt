package ru.ktsstudio.reo.ui.measurement.create.adapters

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.ktsstudio.common.ui.adapter.BaseListAdapter
import ru.ktsstudio.common.ui.adapter.delegates.AddEntityAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.AddEntityItem
import ru.ktsstudio.common.ui.adapter.delegates.CardBottomCornersAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardCornersItem
import ru.ktsstudio.common.ui.adapter.delegates.CardDividerAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardEmptyLine
import ru.ktsstudio.common.ui.adapter.delegates.CardEmptyLineAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardTitleItem
import ru.ktsstudio.common.ui.adapter.delegates.CardTitleItemWitAccentAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardTopCornersAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.DividerItem
import ru.ktsstudio.common.ui.adapter.delegates.ListStateItem
import ru.ktsstudio.common.ui.adapter.delegates.titles.main.TitleAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.titles.main.TitleItem
import ru.ktsstudio.common.ui.adapter.delegates.titles.small.SmallTitleAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.titles.small.SmallTitleItem
import ru.ktsstudio.common.utils.isClassEqualTo
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementComposite
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementMediaCategory
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.reo.presentation.measurement.create_measurement.MeasurementEntity

/**
 * Created by Igor Park on 16/11/2020.
 */
class CreateMeasurementAdapter(
    onRetry: () -> Unit,
    onAddEntity: (MeasurementEntity) -> Unit,
    onContainerClick: (Long, Boolean) -> Unit,
    onDeleteMedia: (Long) -> Unit,
    onAddMedia: (MeasurementMediaCategory) -> Unit,
    onTextChanged: (String) -> Unit
) : BaseListAdapter(CreateMeasurementItemCallback()) {

    init {
        sequenceOf(
            TitleAdapterDelegate(),
            SmallTitleAdapterDelegate(),
            AddEntityAdapterDelegate(onAddEntity),
            ContainerItemAdapterDelegate(onContainerClick),
            MeasurementMediaAdapter(
                onDeleteMedia,
                onAddMedia
            ),
            CommentaryAdapterDelegate(
                onTextChanged
            ),
            CardEmptyLineAdapterDelegate(),
            CardBottomCornersAdapterDelegate(),
            CardTopCornersAdapterDelegate(),
            CardDividerAdapterDelegate(),
            CardTitleItemWitAccentAdapterDelegate(),
            MorphologyCardAdapterDelegate()
        )
            .forEach { delegatesManager.addDelegate(it) }
        delegatesManager.withDefaultDelegates(onRetry)
    }

    class CreateMeasurementItemCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (!oldItem.isClassEqualTo(newItem)) return false
            return when (oldItem) {
                is CommentaryItem,
                is CardEmptyLine,
                is CardCornersItem,
                is DividerItem,
                is ListStateItem -> true
                is TitleItem -> oldItem.text == (newItem as TitleItem).text
                is CardTitleItem -> oldItem.text == (newItem as CardTitleItem).text
                is AddEntityItem<*> -> oldItem.text == (newItem as AddEntityItem<*>).text
                is SmallTitleItem -> oldItem.text == (newItem as SmallTitleItem).text
                is MeasurementComposite.Container -> oldItem.id == (newItem as MeasurementComposite.Container).id
                is MorphologyItem -> {
                    oldItem.localId == (newItem as MorphologyItem).localId
                }
                is MeasurementMediaListItem -> oldItem.category == (newItem as MeasurementMediaListItem).category
                else -> false
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
            return when (newItem) {
                // todo ???
                is CommentaryItem -> newItem.text
                else -> Unit
            }
        }
    }
}
