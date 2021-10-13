package ru.ktsstudio.reo.ui.measurement.morphology.item_info

import android.os.Bundle
import android.view.View
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
import ru.ktsstudio.reo.presentation.measurement.morphology.item_info.EditMorphologyItemUiState
import ru.ktsstudio.reo.presentation.measurement.morphology.item_info.EditMorphologyItemViewModel
import ru.ktsstudio.reo.ui.measurement.edit_waste_type.WasteTypeFieldsAdapter
import ru.ktsstudio.utilities.extensions.hideKeyboard
import ru.ktsstudio.utilities.extensions.toast
import javax.inject.Inject

/**
 * Created by Igor Park on 25/10/2020.
 */
class EditMorphologyItemFragment : BaseFragment(R.layout.fragment_edit_container) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val args: EditMorphologyItemFragmentArgs by navArgs()

    private val measurementId: Long
        get() = args.measurementId
    private val morphologyId: Long?
        get() = args.morphologyId?.toLong()

    private var categoryFieldsAdapter: WasteTypeFieldsAdapter by autoCleared()

    private val viewModel: EditMorphologyItemViewModel by viewModels { factory }

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
        super.onFullDestroy()
        requireActivity().hideKeyboard()
    }

    private fun inject() {
        ComponentRegistry.get<EditMeasurementComponent>()
            .editMorphologyItemComponentFactory()
            .create(measurementId, morphologyId)
            .inject(this)
    }

    private fun initToolbar() = with(requireView().findViewById<Toolbar>(R.id.toolbar)) {
        val editMode = morphologyId != null
        val titleRes = if (editMode) {
            R.string.edit_morphology_change_category_title
        } else {
            R.string.edit_morphology_add_category_title
        }

        setTitle(titleRes)
        setNavigationIcon(R.drawable.ic_arrow_left)
        setNavigationOnClickListener {
            requireActivity().hideKeyboard()
            findNavController().popBackStack()
        }

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
    }

    private fun initViews() {
        categoryFieldsAdapter = WasteTypeFieldsAdapter(
            onTextChanged = viewModel::onFieldChanged,
            onFocusChanged = viewModel::onFocusChanged,
            onRetry = viewModel::retry
        )
        with(list) {
            adapter = categoryFieldsAdapter
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
    }

    private fun bindViewModel() {
        with(viewModel) {
            viewLifecycleOwner.observeNotNull(state, ::renderState)
            viewLifecycleOwner.observeNotNull(error, { toast(it) })
        }
    }

    private fun renderState(state: EditMorphologyItemUiState) {
        categoryFieldsAdapter.setDataWithState(
            data = state.fields,
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
