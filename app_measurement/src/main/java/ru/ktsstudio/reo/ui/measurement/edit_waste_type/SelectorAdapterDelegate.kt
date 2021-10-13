package ru.ktsstudio.reo.ui.measurement.edit_waste_type

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_selector_field.*
import ru.ktsstudio.common.presentation.filter.UiFilterItem
import ru.ktsstudio.common.ui.base_spinner.FilterAdapter
import ru.ktsstudio.common.utils.setRequiredSign
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerField
import ru.ktsstudio.utilities.extensions.inflate

/**
 * Created by Igor Park on 25/10/2020.
 */
class SelectorAdapterDelegate(
    private val onCategorySelected: (ContainerDataType, String) -> Unit
) : AbsListItemAdapterDelegate<
    ContainerField.Selector,
    Any,
    SelectorAdapterDelegate.ViewHolder
    >() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            parent.inflate(R.layout.item_selector_field),
            onCategorySelected
        )
    }

    override fun isForViewType(
        item: Any,
        items: MutableList<Any>,
        position: Int
    ): Boolean {
        return item is ContainerField.Selector
    }

    override fun onBindViewHolder(
        item: ContainerField.Selector,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            holder.configSelector(payloads.first() as ContainerField.Selector)
        }
    }

    class ViewHolder(
        override val containerView: View,
        onCategorySelected: (ContainerDataType, String) -> Unit
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        private var dataType: ContainerDataType? = null
        private var wasteCategoriesAdapter = FilterAdapter(containerView.context)

        init {
            wasteCategoriesDropdown.setAdapter(wasteCategoriesAdapter)
            wasteCategoriesDropdown.onItemClickListener =
                AdapterView.OnItemClickListener { parent, _, position, _ ->
                    val item = parent.getItemAtPosition(position) as UiFilterItem
                    wasteCategoriesDropdown.clearFocus()
                    dataType?.let { onCategorySelected(it, item.id) }
                }
        }

        fun bind(item: ContainerField.Selector) {
            dataType = item.dataType
            configSelector(item)
            wasteCategoriesDropdown.setHint(item.hint)
            title.text = containerView.resources.getString(item.title)
                .setRequiredSign(item.isRequired)
        }

        fun configSelector(item: ContainerField.Selector) {
            wasteCategoriesAdapter.setItems(item.dropDownItems)
            wasteCategoriesDropdown.setText(item.value)
            wasteCategoriesDropdown.isEnabled = item.dropDownItems.isNotEmpty()
            wasteCategoriesDropdown.dismissDropDown()
        }
    }
}
