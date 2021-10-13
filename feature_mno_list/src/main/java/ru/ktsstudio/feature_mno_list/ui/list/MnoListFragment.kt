package ru.ktsstudio.feature_mno_list.ui.list

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_mno_list.*
import ru.ktsstudio.common.ui.fragment.BaseFragment
import ru.ktsstudio.common.ui.sync.SyncHost
import ru.ktsstudio.common.utils.navigateSafe
import ru.ktsstudio.common.utils.keyboard.setKeyboardListener
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.setStroke
import ru.ktsstudio.common.utils.setValueWithoutEventTrigger
import ru.ktsstudio.common.utils.updateDrawableLayer
import ru.ktsstudio.common.utils.view.AfterTextChangedWatcher
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.feature_mno_list.R
import ru.ktsstudio.feature_mno_list.di.list.MnoListComponent
import ru.ktsstudio.feature_mno_list.presentation.list.MnoListUiState
import ru.ktsstudio.feature_mno_list.presentation.list.MnoListViewModel
import ru.ktsstudio.feature_mno_list.ui.list.adapter.MnoListAdapter
import ru.ktsstudio.utilities.extensions.hideKeyboard
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 30.09.2020.
 */
internal class MnoListFragment : BaseFragment(R.layout.fragment_mno_list) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private var mnoListAdapter: MnoListAdapter by autoCleared()
    private var searchInput: EditText by autoCleared()
    private var filterButton: ImageView by autoCleared()
    private var filterSetIndicator: View by autoCleared()

    private val viewModel: MnoListViewModel by viewModels { factory }
    private val syncHost: SyncHost
        get() = parentFragment as SyncHost
    private val searchTextListener = AfterTextChangedWatcher {
        viewModel.updateSearchQuery(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentRegistry.get<MnoListComponent>().inject(this)
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
        initMnoList()
        bindViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDetach()
    }

    override fun onFullDestroy() {
        super.onFullDestroy()
        ComponentRegistry.clear<MnoListComponent>()
    }

    private fun initToolbar() {
        with(requireView().findViewById<Toolbar>(R.id.toolbar)) {
            setTitle(R.string.mno_list_title)
            inflateMenu(R.menu.sync_menu)
            menu.findItem(R.id.action_refresh).actionView.setOnClickListener {
                syncHost.refresh()
            }
        }
    }

    private fun initFilterView() {
        searchInput = requireView().findViewById(R.id.searchInput)
        searchInput.setHint(R.string.mno_list_search_hint)
        filterButton = requireView().findViewById(R.id.filterImageView)
        filterButton.updateDrawableLayer(R.id.strokeLayer) {
            it.setStroke(
                strokeWidth = resources.getDimensionPixelSize(R.dimen.strokeWidth),
                color = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            )
        }
        filterSetIndicator = requireView().findViewById(R.id.filterSetIndicator)
    }

    private fun initMnoList() {
        mnoListAdapter = MnoListAdapter(viewModel::retry) {
            requireActivity().hideKeyboard()
            viewModel.openDetails(it)
        }
        with(list) {
            adapter = mnoListAdapter
            setHasFixedSize(true)
        }
    }

    private fun bindViewModel() = with(viewModel) {
        searchInput.addTextChangedListener(searchTextListener)
        filterButton.setOnClickListener { openFilter() }
        viewLifecycleOwner.observeNotNull(state, ::renderState)
    }

    private fun renderState(state: MnoListUiState) {
        searchInput.setValueWithoutEventTrigger(state.searchQuery, searchTextListener)
        filterSetIndicator.isVisible = state.isFilterSet

        mnoListAdapter.setDataWithState(
            data = state.data,
            isNextPageError = false,
            isNextPageLoading = false,
            isPrevPageError = false,
            isPrevPageLoading = false,
            isLoading = state.loading,
            error = state.error
        )
    }
}
