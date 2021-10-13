package ru.ktsstudio.app_verification.ui.filter

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_object_filter.*
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.di.app.VerificationAppComponent
import ru.ktsstudio.app_verification.domain.object_filter.ObjectFilterItem
import ru.ktsstudio.app_verification.presentation.object_filter.ObjectFilterField
import ru.ktsstudio.app_verification.presentation.object_filter.ObjectFilterUiItem
import ru.ktsstudio.common.presentation.filter.FilterDataUiState
import ru.ktsstudio.common.presentation.filter.FilterViewModel
import ru.ktsstudio.common.presentation.filter.UiFilterItem
import ru.ktsstudio.common.ui.filter.FilterAdapter
import ru.ktsstudio.common.ui.fragment.BaseFragment
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.common_registry.ComponentRegistry
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 19.11.2020.
 */
internal class ObjectFilterFragment : BaseFragment(R.layout.fragment_object_filter) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: @JvmSuppressWildcards FilterViewModel<
        ObjectFilterField,
        ObjectFilterItem,
        ObjectFilterUiItem> by viewModels { factory }

    private var statusAdapter: FilterAdapter by autoCleared()
    private var wasteManagementTypeAdapter: FilterAdapter by autoCleared()
    private var regionsAdapter: FilterAdapter by autoCleared()
    private var surveyStatusAdapter: FilterAdapter by autoCleared()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentRegistry.get<VerificationAppComponent>()
            .objectFilterComponent()
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
        wasteManagementTypeAdapter = FilterAdapter(requireContext())
        wasteManagementTypeDropdown.setAdapter(wasteManagementTypeAdapter)
        wasteManagementTypeDropdown.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val item = parent.getItemAtPosition(position) as UiFilterItem
            wasteManagementTypeDropdown.clearFocus()
            viewModel.changeFilterValue(ObjectFilterField.WASTE_MANAGEMENT_TYPE, item.id)
        }

        regionsAdapter = FilterAdapter(requireContext())
        regionDropdown.setAdapter(regionsAdapter)
        regionDropdown.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val item = parent.getItemAtPosition(position) as UiFilterItem
            regionDropdown.clearFocus()
            viewModel.changeFilterValue(ObjectFilterField.REGION, item.id)
        }

        surveyStatusAdapter = FilterAdapter(requireContext())
        surveyStatusDropdown.setAdapter(surveyStatusAdapter)
        surveyStatusDropdown.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val item = parent.getItemAtPosition(position) as UiFilterItem
            surveyStatusDropdown.clearFocus()
            viewModel.changeFilterValue(ObjectFilterField.SURVEY_STATUS, item.id)
        }
    }

    private fun bindViewModel() = with(viewModel) {
        applyButton.setOnClickListener { applyFilter() }
        viewLifecycleOwner.observeNotNull(state, ::applyState)
        viewLifecycleOwner.observeNotNull(filterApplied, { findNavController().popBackStack() })
    }

    private fun applyState(state: FilterDataUiState<ObjectFilterField, ObjectFilterUiItem>) {
        val wasteManagementTypes = state[ObjectFilterField.WASTE_MANAGEMENT_TYPE].orEmpty()
            .mapNotNull {
                it as? ObjectFilterUiItem.WasteManagementType
            }
        wasteManagementTypeAdapter.setItems(wasteManagementTypes)
        val selectedWasteManagementType = wasteManagementTypes.find { it.isSelected }
        wasteManagementTypeDropdown.setText(selectedWasteManagementType?.title.orEmpty())

        val regions = state[ObjectFilterField.REGION].orEmpty()
            .mapNotNull {
                it as? ObjectFilterUiItem.Region
            }
        regionsAdapter.setItems(regions)
        val selectedRegion = regions.find { it.isSelected }
        regionDropdown.setText(selectedRegion?.title.orEmpty())

        val surveyStatuses = state[ObjectFilterField.SURVEY_STATUS].orEmpty()
            .filterIsInstance<ObjectFilterUiItem.SurveyStatus>()
        surveyStatusAdapter.setItems(surveyStatuses)
        val selectedSurveyStatus = surveyStatuses.find { it.isSelected }
        surveyStatusDropdown.setText(selectedSurveyStatus?.title.orEmpty())
    }
}
