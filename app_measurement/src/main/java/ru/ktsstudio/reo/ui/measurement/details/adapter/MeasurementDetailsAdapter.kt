package ru.ktsstudio.reo.ui.measurement.details.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.ktsstudio.common.ui.adapter.BaseListAdapter
import ru.ktsstudio.common.ui.adapter.delegates.CardBottomCornersAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardCornersItem
import ru.ktsstudio.common.ui.adapter.delegates.CardDividerAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardEmptyLine
import ru.ktsstudio.common.ui.adapter.delegates.CardEmptyLineAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardTitleItem
import ru.ktsstudio.common.ui.adapter.delegates.CardTitleItemAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardTitleItemWitAccentAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardTopCornersAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.DividerItem
import ru.ktsstudio.common.ui.adapter.delegates.DoubleLabeledCardValueAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.DoubleLabeledValueAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.DoubleLabeledValueItem
import ru.ktsstudio.common.ui.adapter.delegates.LabeledCardValueAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.LabeledImageAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.LabeledImageListItem
import ru.ktsstudio.common.ui.adapter.delegates.LabeledValueAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.LabeledValueItem
import ru.ktsstudio.common.ui.adapter.delegates.ListStateItem
import ru.ktsstudio.common.ui.adapter.delegates.titles.main.TitleAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.titles.main.TitleItem
import ru.ktsstudio.common.utils.isClassEqualTo
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.reo.ui.measurement.create.adapters.MorphologyCardAdapterDelegate

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */
class MeasurementDetailsAdapter(
    onRetry: () -> Unit
) : BaseListAdapter(MeasurementDetailsItemCallback()) {

    init {
        sequenceOf(
            TitleAdapterDelegate(),
            LabeledValueAdapterDelegate(),
            LabeledCardValueAdapterDelegate(),
            CardBottomCornersAdapterDelegate(),
            CardTopCornersAdapterDelegate(),
            CardTitleItemAdapterDelegate(),
            CardTitleItemWitAccentAdapterDelegate(),
            CardDividerAdapterDelegate(),
            CardEmptyLineAdapterDelegate(),
            DoubleLabeledValueAdapterDelegate(),
            DoubleLabeledCardValueAdapterDelegate(),
            MorphologyCardAdapterDelegate(),
            LabeledImageAdapterDelegate()
        ).forEach { delegatesManager.addDelegate(it) }
        delegatesManager.withDefaultDelegates(onRetry)
    }

    class MeasurementDetailsItemCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (!oldItem.isClassEqualTo(newItem)) return false
            return when (oldItem) {
                is TitleItem -> oldItem.text == (newItem as TitleItem).text
                is LabeledValueItem -> oldItem.label == (newItem as LabeledValueItem).label
                is CardTitleItem -> oldItem.text == (newItem as CardTitleItem).text
                is CardCornersItem -> oldItem.isTop == (newItem as CardCornersItem).isTop

                is MorphologyItem -> {
                    oldItem.localId == (newItem as MorphologyItem).localId
                }
                is CardEmptyLine -> oldItem.isNested == (newItem as CardEmptyLine).isNested
                is LabeledImageListItem -> oldItem.label == (newItem as LabeledImageListItem).label
                is DoubleLabeledValueItem -> oldItem.leftLabel == (newItem as DoubleLabeledValueItem).leftLabel &&
                    oldItem.rightLabel == newItem.rightLabel
                is DividerItem,
                is ListStateItem -> true
                else -> false
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }
    }
}
