package ru.ktsstudio.reo.ui.measurement.edit_mixed_container.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_open_container_field.*
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerField
import ru.ktsstudio.utilities.extensions.inflate

/**
 * Created by Igor Park on 25/10/2020.
 */
class AutoFillAdapterDelegate : AbsListItemAdapterDelegate<
    ContainerField.Autofill,
    Any,
    AutoFillAdapterDelegate.ViewHolder
    >() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            parent.inflate(R.layout.item_autofill_container_field)
        )
    }

    override fun isForViewType(
        item: Any,
        items: MutableList<Any>,
        position: Int
    ): Boolean {
        return item is ContainerField.Autofill
    }

    override fun onBindViewHolder(
        item: ContainerField.Autofill,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(item: ContainerField.Autofill) {
            title.setText(item.title)
            input.setHint(item.hint)
            input.setText(item.value)
        }
    }
}
