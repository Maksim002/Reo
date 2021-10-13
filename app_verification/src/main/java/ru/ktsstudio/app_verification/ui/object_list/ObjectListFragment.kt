package ru.ktsstudio.app_verification.ui.object_list

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.android.synthetic.main.fragment_object_list.*
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.databinding.FragmentObjectListBinding
import ru.ktsstudio.app_verification.di.object_list.ObjectListComponent
import ru.ktsstudio.app_verification.domain.object_list.ObjectSort
import ru.ktsstudio.app_verification.presentation.object_list.ObjectListUiState
import ru.ktsstudio.app_verification.presentation.object_list.ObjectListViewModel
import ru.ktsstudio.common.ui.sync.SyncHost
import ru.ktsstudio.common.utils.keyboard.setKeyboardListener
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.setStroke
import ru.ktsstudio.common.utils.setValueWithoutEventTrigger
import ru.ktsstudio.common.utils.updateDrawableLayer
import ru.ktsstudio.common.utils.view.AfterTextChangedWatcher
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.utilities.extensions.hideKeyboard
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.11.2020.
 */
class ObjectListFragment : Fragment(R.layout.fragment_object_list) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val binding: FragmentObjectListBinding by viewBinding(FragmentObjectListBinding::bind)
    private var objectListAdapter: ObjectListAdapter by autoCleared()
    private var searchInput: EditText by autoCleared()
    private var filterButton: ImageView by autoCleared()
    private var filterSetIndicator: View by autoCleared()
    private var sortButton: ImageView by autoCleared()

    private val viewModel: ObjectListViewModel by viewModels { factory }
    private val searchTextListener = AfterTextChangedWatcher {
        viewModel.updateSearchQuery(it)
    }
    private val syncHost: SyncHost
        get() = parentFragment as SyncHost

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ObjectListComponent.create().inject(this)

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
        initList()
        bindViewModel()
    }

    private fun initToolbar() {
        with(binding.appBar.toolbar) {
            binding.appBar.multilineTitle.setText(R.string.verification_object_list_title)
            inflateMenu(R.menu.sync_menu)
            menu.findItem(R.id.action_refresh).actionView.setOnClickListener {
                syncHost.refresh()
            }
        }
    }

    private fun initFilterView() {
        searchInput = requireView().findViewById(R.id.searchInput)
        searchInput.setHint(R.string.object_list_search_hint)
        filterButton = requireView().findViewById(R.id.filterImageView)
        filterButton.updateDrawableLayer(R.id.strokeLayer) {
            it.setStroke(
                strokeWidth = resources.getDimensionPixelSize(R.dimen.strokeWidth),
                color = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            )
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

    private fun initList() {
        objectListAdapter = ObjectListAdapter(viewModel::retry) {
            requireActivity().hideKeyboard()
            viewModel.openInspection(it)
        }
        with(binding.list) {
            adapter = objectListAdapter
            setHasFixedSize(true)
        }
    }

    private fun scrollToTopOnNextData() {
        objectListAdapter.registerAdapterDataObserver(
            object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                    list.scrollToPosition(0)
                    objectListAdapter.unregisterAdapterDataObserver(this)
                }
            }
        )
    }

    private fun bindViewModel() = with(viewModel) {
        searchInput.addTextChangedListener(searchTextListener)
        filterButton.setOnClickListener { openFilter() }
        viewLifecycleOwner.observeNotNull(state, ::renderState)
    }

    private fun renderState(state: ObjectListUiState) {
        searchInput.setValueWithoutEventTrigger(state.searchQuery, searchTextListener)
        filterSetIndicator.isVisible = state.isFilterSet

        objectListAdapter.setDataWithState(
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
            inflate(R.menu.menu_object_sort)
            setOnMenuItemClickListener { item ->
                val selectedSort = when (item.itemId) {
                    R.id.object_sort_by_name_descending -> {
                        ObjectSort.NAME_DESCENDING
                    }
                    R.id.object_sort_by_name_ascending -> {
                        ObjectSort.NAME_ASCENDING
                    }
                    R.id.object_sort_by_created_at_descending -> {
                        ObjectSort.CREATED_AT_DESCENDING
                    }
                    R.id.object_sort_by_created_at_ascending -> {
                        ObjectSort.CREATED_AT_ASCENDING
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
