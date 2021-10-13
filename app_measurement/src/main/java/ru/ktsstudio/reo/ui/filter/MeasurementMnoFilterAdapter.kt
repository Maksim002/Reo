package ru.ktsstudio.reo.ui.filter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_measurement_mno_filter.*
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.presentation.measurement_filter.MeasurementFilterUiItem
import ru.ktsstudio.utilities.extensions.inflate

/**
 * @author Maxim Myalkin (MaxMyalkin) on 19.10.2020.
 */
class MeasurementMnoFilterAdapter(context: Context) : ArrayAdapter<MeasurementFilterUiItem.Mno>(
    context,
    R.layout.item_filter
), Filterable {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: Holder?
        if (view == null) {
            view = parent.inflate(R.layout.item_measurement_mno_filter)
            holder = Holder(view)
            view.tag = holder
        } else {
            holder = view.tag as Holder
        }
        getItem(position)?.let(holder::bind)
        return view
    }

    fun setItems(newItems: List<MeasurementFilterUiItem.Mno>) {
        clear()
        addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun convertResultToString(resultValue: Any?): CharSequence {
                return when (resultValue) {
                    is MeasurementFilterUiItem.Mno -> resultValue.title
                    else -> super.convertResultToString(resultValue)
                }
            }

            override fun performFiltering(constraint: CharSequence?): FilterResults? = null
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {}
        }
    }

    class Holder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: MeasurementFilterUiItem.Mno) {
            itemTitleTextView.text = item.title
            itemCategoryTextView.text = item.category
            itemAddressTextView.text = item.address
            listOf(
                containerView,
                itemTitleTextView,
                itemCategoryTextView,
                itemAddressTextView
            ).forEach { it.isActivated = item.isSelected }
        }
    }
}
