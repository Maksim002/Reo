package ru.ktsstudio.reo.ui.measurement.edit_separate_container

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_edit_container.*
import ru.ktsstudio.common.ui.fragment.BaseFragment
import ru.ktsstudio.common.ui.view.MarginInternalItemDecoration
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.setupCustomLayoutParams
import ru.ktsstudio.common.utils.view.HideElevationScrollListener
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.di.create_measurement.EditMeasurementComponent
import ru.ktsstudio.reo.presentation.measurement.edit_separate_container.EditSeparateContainerUiState
import ru.ktsstudio.reo.presentation.measurement.edit_separate_container.EditSeparateContainerViewModel
import ru.ktsstudio.utilities.extensions.hideKeyboard
import ru.ktsstudio.utilities.extensions.toast
import javax.inject.Inject

/**
 * Created by Igor Park on 25/10/2020.
 */
class EditSeparateContainerFragment : BaseFragment(R.layout.fragment_edit_container) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val args: EditSeparateContainerFragmentArgs by navArgs()

    private val measurementId: Long
        get() = args.measurementId
    private val containerId: Long?
        get() = args.containerId?.toLong()
    private val mnoContainerId: String?
        get() = args.mnoContainerId
    private val containerTypeId: String?
        get() = args.containerTypeId

    private var containerFieldsAdapter: SeparateContainerFieldsAdapter by autoCleared()

    private val viewModel: EditSeparateContainerViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViews()
        initListeners()
        bindViewModel()
    }

    override fun onFullDestroy() {
        requireActivity().hideKeyboard()
    }

    private fun inject() {
        ComponentRegistry.get<EditMeasurementComponent>()
            .editSeparateContainerComponentFactory()
            .create(
                measurementId = measurementId,
                containerId = containerId,
                mnoContainerId = mnoContainerId,
                containerTypeId = containerTypeId
            )
            .inject(this)
    }

    private fun initToolbar() = with(requireView().findViewById<Toolbar>(R.id.toolbar)) {
        val editMode = containerId != null
        val title = if (editMode) {
            R.string.edit_mixed_container_edit_mode_title
        } else {
            R.string.measurement_add_container_title
        }
        setTitle(title)
        if (editMode) {
            inflateMenu(R.menu.edit_measurement_menu)
            menu.findItem(R.id.action_delete)
                .setupCustomLayoutParams(
                    R.drawable.ic_delete,
                    R.string.action_delete,
                    ContextCompat.getColor(requireContext(), R.color.danger_normal),
                    viewModel::deleteData
                )
        }

        setNavigationIcon(R.drawable.ic_arrow_left)
        setNavigationOnClickListener { viewModel.clearData() }
    }

    private fun initViews() {
        containerFieldsAdapter = SeparateContainerFieldsAdapter(
            onTextChanged = viewModel::onTextChanged,
            onFocusChanged = { type, hasFocus -> /* noop */ },
            onWasteTypeClick = { wasteTypeId ->
                requireActivity().hideKeyboard()
                viewModel.openWasteTypeEditing(wasteTypeId)
            },
            onAddWasteTypeClick = {
                requireActivity().hideKeyboard()
                viewModel.openWasteTypeEditing(null)
            },
            onRetry = viewModel::retry
        )
        with(list) {
            adapter = containerFieldsAdapter
            addItemDecoration(getSpaceItemDecoration())
            setHasFixedSize(true)
            addOnScrollListener(
                HideElevationScrollListener(
                    bottomBlock,
                    resources.getDimensionPixelSize(R.dimen.default_elevation)
                )
            )
        }
    }

    private fun initListeners() {
        saveContainer.setOnClickListener {
            requireActivity().hideKeyboard()
            viewModel.saveData()
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.clearData()
                }
            }
        )
    }

    private fun bindViewModel() {
        with(viewModel) {
            viewLifecycleOwner.observeNotNull(state, ::renderState)
            viewLifecycleOwner.observeNotNull(error, { toast(it) })
            viewLifecycleOwner.observeNotNull(dataCleared, { findNavController().popBackStack() })
        }
    }

    private fun renderState(state: EditSeparateContainerUiState) {
        containerFieldsAdapter.setDataWithState(
            data = state.containerFields,
            isNextPageLoading = false,
            isNextPageError = false,
            isPrevPageLoading = false,
            isPrevPageError = false,
            isLoading = state.isLoading,
            error = state.error
        )
        saveContainer.isEnabled = state.isReady
    }

    private fun getSpaceItemDecoration(): RecyclerView.ItemDecoration {
        val margin = requireContext().resources
            .getDimensionPixelSize(R.dimen.default_double_padding)
        return MarginInternalItemDecoration(
            spaceHeight = margin,
            needSpace = { true }
        )
    }
}
