package ru.ktsstudio.reo.ui.measurement.morphology.section

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_edit_morphology.*
import ru.ktsstudio.common.ui.fragment.BaseFragment
import ru.ktsstudio.common.ui.view.MarginInternalItemDecoration
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.di.create_measurement.EditMeasurementComponent
import ru.ktsstudio.reo.presentation.measurement.morphology.section.EditMorphologyUiState
import ru.ktsstudio.reo.presentation.measurement.morphology.section.EditMorphologyViewModel
import ru.ktsstudio.utilities.extensions.toast
import javax.inject.Inject

/**
 * Created by Igor Park on 25/10/2020.
 */
class EditMorphologyFragment : BaseFragment(R.layout.fragment_edit_morphology) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val args: EditMorphologyFragmentArgs by navArgs()

    private val measurementId: Long
        get() = args.measurementId

    private var categoriesAdapter: MorphologyAdapter by autoCleared()

    private val viewModel: EditMorphologyViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViews()
        bindViewModel()
    }

    private fun inject() {
        ComponentRegistry.get<EditMeasurementComponent>()
            .editMorphologyComponentFactory()
            .create(measurementId)
            .inject(this)
    }

    private fun initToolbar() = with(requireView().findViewById<Toolbar>(R.id.toolbar)) {
        setTitle(R.string.edit_morphology_add_category_title)
        setNavigationIcon(R.drawable.ic_arrow_left)
        setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun initViews() {
        categoriesAdapter =
            MorphologyAdapter(
                onCategoryClick = viewModel::openMorphologyEditing,
                onAddCategoryClick = { viewModel.openMorphologyEditing(null) },
                onRetry = viewModel::retry
            )
        with(list) {
            adapter = categoriesAdapter
            addItemDecoration(getSpaceItemDecoration())
            setHasFixedSize(true)
        }
    }

    private fun bindViewModel() {
        with(viewModel) {
            viewLifecycleOwner.observeNotNull(state, ::renderState)
            viewLifecycleOwner.observeNotNull(error, { toast(it) })
        }
    }

    private fun renderState(state: EditMorphologyUiState) {
        categoriesAdapter.setDataWithState(
            data = state.morphologyList,
            isNextPageLoading = false,
            isNextPageError = false,
            isPrevPageLoading = false,
            isPrevPageError = false,
            isLoading = state.isLoading,
            error = state.error
        )
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
