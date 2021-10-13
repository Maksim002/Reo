package ru.ktsstudio.reo.ui.filter

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_measurement_filter.*
import kotlinx.android.synthetic.main.fragment_measurement_filter.applyButton
import kotlinx.android.synthetic.main.fragment_mno_filter.*
import ru.ktsstudio.common.presentation.filter.FilterDataUiState
import ru.ktsstudio.common.presentation.filter.FilterViewModel
import ru.ktsstudio.common.presentation.filter.UiFilterItem
import ru.ktsstudio.common.ui.fragment.BaseFragment
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.domain.measurement.MeasurementFilterItem
import ru.ktsstudio.reo.presentation.measurement_filter.MeasurementFilterField
import ru.ktsstudio.reo.presentation.measurement_filter.MeasurementFilterUiItem
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.reo.di.app.MeasurementAppComponent
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 20.10.2020.
 */
class MeasurementFilterFragment : BaseFragment(R.layout.fragment_measurement_filter) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: @JvmSuppressWildcards FilterViewModel<
        MeasurementFilterField,
        MeasurementFilterItem,
        MeasurementFilterUiItem> by viewModels { factory }

    private var mnoAdapter: MeasurementMnoFilterAdapter by autoCleared()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentRegistry.get<MeasurementAppComponent>()
            .measurementListComponent()
            .filterComponent()
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
        mnoAdapter = MeasurementMnoFilterAdapter(requireContext())
        mnoDropdown.setAdapter(mnoAdapter)
        mnoDropdown.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val item = parent.getItemAtPosition(position) as UiFilterItem
            mnoDropdown.clearFocus()
            viewModel.changeFilterValue(MeasurementFilterField.MNO, item.id)
        }
    }

    private fun bindViewModel() = with(viewModel) {
        applyButton.setOnClickListener { applyFilter() }
        viewLifecycleOwner.observeNotNull(state, ::applyState)
        viewLifecycleOwner.observeNotNull(filterApplied, { findNavController().popBackStack() })
    }

    private fun applyState(state: FilterDataUiState<MeasurementFilterField, MeasurementFilterUiItem>) {
        val mnos = state[MeasurementFilterField.MNO].orEmpty()
            .filterIsInstance<MeasurementFilterUiItem.Mno>()
        mnoAdapter.setItems(mnos)
        val selectedMno = mnos.find { it.isSelected }
        mnoDropdown.setText(selectedMno?.title.orEmpty())
    }
}
