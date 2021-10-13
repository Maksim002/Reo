package ru.ktsstudio.reo.ui.measurement.add_container

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_add_container.*
import ru.ktsstudio.common.presentation.filter.UiFilterItem
import ru.ktsstudio.common.ui.base_spinner.FilterAdapter
import ru.ktsstudio.common.ui.fragment.BaseFragment
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.di.create_measurement.EditMeasurementComponent
import ru.ktsstudio.reo.presentation.measurement.add_container.AddContainerUiState
import ru.ktsstudio.reo.presentation.measurement.add_container.AddContainerViewModel
import ru.ktsstudio.utilities.extensions.toast
import javax.inject.Inject

/**
 * Created by Igor Park on 14/10/2020.
 */
class AddContainerFragment : BaseFragment(R.layout.fragment_add_container) {

    private val args: AddContainerFragmentArgs by navArgs()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private var mnoContainersAdapter: MnoContainerAdapter by autoCleared()
    private var containerTypesAdapter: FilterAdapter by autoCleared()

    private val viewModel: AddContainerViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViews()
        initListeners()
        bindViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDetach()
    }

    private fun inject() {
        ComponentRegistry.get<EditMeasurementComponent>()
            .addContainerComponent()
            .inject(this)
    }

    private fun initToolbar() = with(requireView().findViewById<Toolbar>(R.id.toolbar)) {
        setTitle(R.string.measurement_add_container_title)
        setNavigationIcon(R.drawable.ic_arrow_left)
        setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun initViews() {
        containerTypesAdapter = FilterAdapter(requireContext())
        containerTypesDropdown.setAdapter(containerTypesAdapter)
        mnoContainersAdapter = MnoContainerAdapter(viewModel::selectMnoContainer)
        mnoContainerList.adapter = mnoContainersAdapter
    }

    private fun initListeners() {
        newContainerButton.setOnClickListener {
            viewModel.selectNewContainer()
        }

        navigateToEditContainer.setOnClickListener {
            viewModel.navigateToEditContainer(args.measurementLocalId)
        }
        containerTypesDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val item = parent.getItemAtPosition(position) as UiFilterItem
                containerTypesDropdown.clearFocus()
                viewModel.selectContainerType(item.id)
            }
        requireView().findViewById<View>(R.id.retryButton)
            .setOnClickListener {
                viewModel.initData()
            }
    }

    private fun bindViewModel() {
        with(viewModel) {
            viewLifecycleOwner.observeNotNull(errorMessage, { toast(it) })
            viewLifecycleOwner.observeNotNull(state, ::renderState)
        }
    }

    private fun renderState(state: AddContainerUiState) = with(state) {
        viewError.isVisible = isErrorVisible
        dataLoading.isVisible = isLoading
        content.isVisible = isContentVisible
        navigateToEditContainer.isEnabled = isOptionSelected

        newContainerButton.isChecked = isNewContainer
        containerTypeGroup.isVisible = isNewContainer
        selectMnoContainerHint.isVisible = mnoContainers.isNotEmpty()

        containerTypesAdapter.setItems(containerTypes)

        selectMnoContainerHint.isVisible = mnoContainers.isNotEmpty()
        mnoContainersAdapter.submitList(mnoContainers)

        val selectedCategory = containerTypes.find { it.isSelected }
        containerTypesDropdown.setText(selectedCategory?.title.orEmpty())
    }
}
