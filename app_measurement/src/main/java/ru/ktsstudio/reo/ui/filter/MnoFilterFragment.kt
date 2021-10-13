package ru.ktsstudio.reo.ui.filter

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_mno_filter.*
import ru.ktsstudio.common.presentation.filter.FilterDataUiState
import ru.ktsstudio.common.presentation.filter.FilterViewModel
import ru.ktsstudio.common.presentation.filter.UiFilterItem
import ru.ktsstudio.common.ui.filter.FilterAdapter
import ru.ktsstudio.common.ui.fragment.BaseFragment
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.di.app.MeasurementAppComponent
import ru.ktsstudio.reo.domain.mno_filter.MnoFilterItem
import ru.ktsstudio.reo.presentation.mno_filter.MnoFilterField
import ru.ktsstudio.reo.presentation.mno_filter.MnoFilterUiItem
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
internal class MnoFilterFragment : BaseFragment(R.layout.fragment_mno_filter) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: @JvmSuppressWildcards FilterViewModel<
        MnoFilterField,
        MnoFilterItem,
        MnoFilterUiItem> by viewModels { factory }

    private var categoryAdapter: FilterAdapter by autoCleared()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentRegistry.get<MeasurementAppComponent>()
            .mnoFilterComponent()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initFilters()
        bindViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDetach()
    }

    private fun initToolbar() {
        with(requireView().findViewById<Toolbar>(R.id.toolbar)) {
            setTitle(R.string.filter_title)
            inflateMenu(R.menu.filter_menu)
            menu.findItem(R.id.action_reset).actionView.setOnClickListener {
                viewModel.clearFilter()
            }
            setNavigationIcon(R.drawable.ic_arrow_left)
            setNavigationOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initFilters() {
        categoryAdapter = FilterAdapter(requireContext())
        categoryDropdown.setAdapter(categoryAdapter)
        categoryDropdown.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val item = parent.getItemAtPosition(position) as UiFilterItem
            categoryDropdown.clearFocus()
            viewModel.changeFilterValue(MnoFilterField.CATEGORY, item.id)
        }
    }

    private fun bindViewModel() = with(viewModel) {
        applyButton.setOnClickListener { applyFilter() }
        viewLifecycleOwner.observeNotNull(state, ::applyState)
        viewLifecycleOwner.observeNotNull(filterApplied, { findNavController().popBackStack() })
    }

    private fun applyState(state: FilterDataUiState<MnoFilterField, MnoFilterUiItem>) {
        val categories = state[MnoFilterField.CATEGORY].orEmpty()
            .mapNotNull {
                it as? MnoFilterUiItem.MnoCategory
            }
        categoryAdapter.setItems(categories)
        val selectedCategory = categories.find { it.isSelected }
        categoryDropdown.setText(selectedCategory?.title.orEmpty())
    }
}
