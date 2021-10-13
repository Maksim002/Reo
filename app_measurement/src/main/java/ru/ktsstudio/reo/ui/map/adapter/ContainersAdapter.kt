package ru.ktsstudio.reo.ui.map.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_container_count.*
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.presentation.map.MnoUiContainer
import ru.ktsstudio.utilities.extensions.inflate

/**
 * Created by Igor Park on 17/04/2020.
 */
class ContainersAdapter : RecyclerView.Adapter<ContainersAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_container_count))
    }

    private var items = listOf<MnoUiContainer>()

    fun setItems(newItems: List<MnoUiContainer>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(container: MnoUiContainer) {
            type.text = container.typeUnit
            quantity.text = containerView.context
                .resources
                .getString(
                    R.string.mno_info_pieces,
                    container.quantityUnit
                )
        }
    }

    override fun getItemCount(): Int = items.size
}
