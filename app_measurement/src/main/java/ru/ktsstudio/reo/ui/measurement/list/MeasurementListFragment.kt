package ru.ktsstudio.reo.ui.measurement.list

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_measurement_list.*
import ru.ktsstudio.common.ui.fragment.BaseFragment
import ru.ktsstudio.common.ui.sync.SyncHost
import ru.ktsstudio.common.utils.keyboard.setKeyboardListener
import ru.ktsstudio.common.utils.navigateSafe
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.setStroke
import ru.ktsstudio.common.utils.setValueWithoutEventTrigger
import ru.ktsstudio.common.utils.updateDrawableLayer
import ru.ktsstudio.common.utils.view.AfterTextChangedWatcher
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.di.app.MeasurementAppComponent
import ru.ktsstudio.reo.domain.measurement.list.MeasurementSort
import ru.ktsstudio.reo.presentation.measurement.list.MeasurementListUiState
import ru.ktsstudio.reo.presentation.measurement.list.MeasurementListViewModel
import ru.ktsstudio.reo.ui.measurement.list.adapter.MeasurementListAdapter
import ru.ktsstudio.reo.ui.tabs.TabsFragmentDirections
import ru.ktsstudio.utilities.extensions.hideKeyboard
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 08.10.2020.
 */
internal class MeasurementListFragment : BaseFragment(R.layout.fragment_measurement_list) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private var measurementListAdapter: MeasurementListAdapter by autoCleared()
    private var searchInput: EditText by autoCleared()
    private var filterButton: ImageView by autoCleared()
    private var filterSetIndicator: View by autoCleared()
    private var sortButton: ImageView by autoCleared()

    private val viewModel: MeasurementListViewModel by viewModels { factory }
    private val syncHost: SyncHost
        get() = parentFragment as SyncHost
    private val searchTextListener = AfterTextChangedWatcher {
        viewModel.updateSearchQuery(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentRegistry.get<MeasurementAppComponent>().measurementListComponent().inject(this)
        setKeyboardListener { isVisible ->
            if (isVisible.not()) {
                searchInput.clearFocus()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initFilterView()
        initMeasurementList()
        addBackButtonCallback()
        bindViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDetach()
    }

    private fun initToolbar() = with(requireView().findViewById<Toolbar>(R.id.toolbar)) {
        setTitle(R.string.measurement_list_title)
        inflateMenu(R.menu.sync_menu)
        menu.findItem(R.id.action_refresh).actionView.setOnClickListener {
            syncHost.refresh()
        }
    }

    private fun initFilterView() {
        searchInput = requireView().findViewById(R.id.searchInput)
        searchInput.setHint(R.string.measurement_list_search_hint)
        filterButton = requireView().findViewById(R.id.filterImageView)
        filterButton.updateDrawableLayer(R.id.strokeLayer) {
            it.setStroke(
                strokeWidth = resources.getDimensionPixelSize(R.dimen.strokeWidth),
                color = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            )
        }
        filterButton.setOnClickListener {
            val direction = TabsFragmentDirections.actionTabsFragmentToMeasurementFilterFragment()
            findNavController().navigateSafe(direction)
        }
        filterSetIndicator = requireView().findViewById(R.id.filterSetIndicator)
        sortButton = requireView().findViewById(R.id.sortImageView)
        sortButton.updateDrawableLayer(R.id.strokeLayer) {
            it.setStroke(
                strokeWidth = resources.getDimensionPixelSize(R.dimen.strokeWidth),
                color = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            )
        }
        sortButton.setOnClickListener {
            showSortPopup()
        }
    }

    private fun initMeasurementList() {
        measurementListAdapter = MeasurementListAdapter(viewModel::retry) {
            requireActivity().hideKeyboard()
            viewModel.openDetails(it)
        }
        with(list) {
            adapter = measurementListAdapter
            setHasFixedSize(true)
        }
    }

    private fun scrollToTopOnNextData() {
        measurementListAdapter.registerAdapterDataObserver(
            object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                    list.scrollToPosition(0)
                    measurementListAdapter.unregisterAdapterDataObserver(this)
                }
            }
        )
    }

    private fun addBackButtonCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finish()
        }
    }

    private fun bindViewModel() = with(viewModel) {
        searchInput.addTextChangedListener(searchTextListener)
        viewLifecycleOwner.observeNotNull(state, ::renderState)
    }

    private fun renderState(state: MeasurementListUiState) {
        searchInput.setValueWithoutEventTrigger(state.searchQuery, searchTextListener)
        filterSetIndicator.isVisible = state.isFilterSet

        measurementListAdapter.setDataWithState(
            data = state.data,
            isNextPageError = false,
            isNextPageLoading = false,
            isPrevPageError = false,
            isPrevPageLoading = false,
            isLoading = state.loading,
            error = state.error
        )
    }

    private fun showSortPopup() {
        with(PopupMenu(requireContext(), sortButton)) {
            inflate(R.menu.menu_measurement_sort)
            setOnMenuItemClickListener { item ->
                val selectedSort = when (item.itemId) {
                    R.id.measurement_sort_status -> {
                        MeasurementSort.STATUS
                    }
                    R.id.measurement_sort_created_at -> {
                        MeasurementSort.CREATED_AT
                    }
                    else -> error("incorrect itemId")
                }
                scrollToTopOnNextData()
                viewModel.selectSort(selectedSort)
                true
            }
            show()
        }
    }
}
