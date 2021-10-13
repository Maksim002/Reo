package ru.ktsstudio.reo.ui.measurement.edit_mixed_container.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_open_container_field.*
import ru.ktsstudio.common.utils.setRequiredSign
import ru.ktsstudio.common.utils.setValueWithoutEventTrigger
import ru.ktsstudio.common.utils.updateInputTypeFormat
import ru.ktsstudio.common.utils.view.AfterTextChangedWatcher
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerField
import ru.ktsstudio.utilities.extensions.inflate

/**
 * Created by Igor Park on 25/10/2020.
 */
class OpenFieldAdapterDelegate(
    private val onTextChanged: (ContainerDataType, String) -> Unit,
    private val onFocusChanged: (ContainerDataType, Boolean) -> Unit
) : AbsListItemAdapterDelegate<
    ContainerField.OpenField,
    Any,
    OpenFieldAdapterDelegate.ViewHolder
    >() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            parent.inflate(R.layout.item_open_container_field),
            onTextChanged,
            onFocusChanged
        )
    }

    override fun isForViewType(
        item: Any,
        items: MutableList<Any>,
        position: Int
    ): Boolean {
        return item is ContainerField.OpenField
    }

    override fun onBindViewHolder(
        item: ContainerField.OpenField,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            holder.updateInput(payloads.first() as ContainerField.OpenField)
        }
    }

    class ViewHolder(
        override val containerView: View,
        onTextChanged: (ContainerDataType, String) -> Unit,
        onFocusChanged: (ContainerDataType, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        private var dataType: ContainerDataType? = null
        private val textListener = AfterTextChangedWatcher { newText ->
            dataType?.let { onTextChanged(it, newText) }
        }

        init {
            input.addTextChangedListener(textListener)
            input.setOnFocusChangeListener { _, hasFocus ->
                dataType?.let { onFocusChanged(it, hasFocus) }
            }
        }

        fun bind(item: ContainerField.OpenField) {
            dataType = item.dataType
            title.text = containerView.resources.getString(item.title)
                .setRequiredSign(item.isRequired)
            input.setHint(item.hint)
            input.updateInputTypeFormat(item.dataType.format)
            updateInput(item)
        }

        fun updateInput(item: ContainerField.OpenField) {
            errorText.isVisible = item.error != null
            errorText.text = item.error?.let { containerView.resources.getString(it) }.orEmpty()
            input.setValueWithoutEventTrigger(item.value.orEmpty(), textListener)
        }
    }
}
