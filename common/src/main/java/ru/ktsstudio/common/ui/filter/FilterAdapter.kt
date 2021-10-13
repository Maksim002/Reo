package ru.ktsstudio.common.ui.filter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_filter.*
import ru.ktsstudio.common.R
import ru.ktsstudio.common.presentation.filter.UiFilterItem
import ru.ktsstudio.utilities.extensions.inflate

/**
 * @author Maxim Myalkin (MaxMyalkin) on 19.10.2020.
 */
class FilterAdapter(context: Context) : ArrayAdapter<UiFilterItem>(context, R.layout.item_filter),
    Filterable {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: Holder?
        if (view == null) {
            view = parent.inflate(R.layout.item_filter)
            holder = Holder(view)
            view.tag = holder
        } else {
            holder = view.tag as Holder
        }
        getItem(position)?.let(holder::bind)
        return view
    }

    fun setItems(newItems: List<UiFilterItem>) {
        clear()
        addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun convertResultToString(resultValue: Any?): CharSequence {
                return when (resultValue) {
                    is UiFilterItem -> resultValue.title
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

        fun bind(item: UiFilterItem) {
            itemTitleTextView.text = item.title
            itemTitleTextView.isActivated = item.isSelected
        }
    }
}