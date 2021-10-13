package ru.ktsstudio.reo.ui.measurement.details

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_measurment_details.*
import ru.ktsstudio.common.ui.adapter.delegates.CardBottomCornersAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardDividerAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardEmptyLineAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardTitleItemAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardTitleItemWitAccentAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.DoubleLabeledCardValueAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.LabeledCardValueAdapterDelegate
import ru.ktsstudio.common.ui.fragment.BaseFragment
import ru.ktsstudio.common.ui.view.MarginInternalItemDecoration
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.view.HideElevationScrollListener
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.di.create_measurement.EditMeasurementComponent
import ru.ktsstudio.reo.di.measurement.details.MeasurementDetailsComponent
import ru.ktsstudio.reo.navigation.measurement.MeasurementReturnTag
import ru.ktsstudio.reo.presentation.measurement.details.MeasurementDetailsUiState
import ru.ktsstudio.reo.presentation.measurement.details.MeasurementDetailsViewModel
import ru.ktsstudio.reo.ui.measurement.create.adapters.MorphologyCardAdapterDelegate
import ru.ktsstudio.reo.ui.measurement.details.adapter.MeasurementDetailsAdapter
import ru.ktsstudio.utilities.extensions.toast
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */
class MeasurementDetailsFragment : BaseFragment(R.layout.fragment_measurment_details) {
    private val args: MeasurementDetailsFragmentArgs by navArgs()

    private val measurementId: Long
        get() = args.measurementId
    private val isPreviewMode: Boolean
        get() = args.isPreviewMode
    private val returnTag: MeasurementReturnTag?
        get() = args.returnTag
            ?.let(MeasurementReturnTag::valueOf)

    private var measurementDetailsAdapter: MeasurementDetailsAdapter by autoCleared()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: MeasurementDetailsViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MeasurementDetailsComponent.create(
            measurementId = measurementId,
            isPreviewMode = isPreviewMode,
            returnTag = returnTag,
            draftHolder = returnTag?.let {
                ComponentRegistry.get<EditMeasurementComponent>()
                    .draftHolder()
            }
        )
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initMnoList()
        initViews()
        bindViewModel()
    }

    private fun bindViewModel() {
        viewLifecycleOwner.observeNotNull(viewModel.state, ::setState)
        viewLifecycleOwner.observeNotNull(viewModel.errorMessage, { toast(it) })
    }

    private fun initToolbar() = with(requireView().findViewById<Toolbar>(R.id.toolbar)) {
        setTitle(R.string.measurement_details_title)
        setNavigationIcon(R.drawable.ic_arrow_left)
        setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun initMnoList() {
        measurementDetailsAdapter = MeasurementDetailsAdapter(viewModel::retry)
        with(list) {
            adapter = measurementDetailsAdapter
            setHasFixedSize(true)
            addItemDecoration(getSpaceItemDecoration())
            addOnScrollListener(
                HideElevationScrollListener(
                    bottomBlock,
                    resources.getDimensionPixelSize(ru.ktsstudio.feature_mno_list.R.dimen.default_elevation)
                )
            )
        }
    }

    private fun initViews() {
        val measurementCardActionText = if (isPreviewMode) {
            when (returnTag) {
                MeasurementReturnTag.MEASUREMENT_DETAILS -> R.string.measurement_details_save_measurement_update
                null,
                MeasurementReturnTag.MNO_DETAILS -> R.string.measurement_create_card
            }
        } else {
            R.string.measurement_details_update_measurement
        }
        updateMeasurementButton.setText(measurementCardActionText)
        updateMeasurementButton.setOnClickListener {
            viewModel.takeMeasurementAction()
        }
    }

    private fun getSpaceItemDecoration(): RecyclerView.ItemDecoration {
        val margin = requireContext().resources
            .getDimensionPixelSize(R.dimen.default_padding)
        return MarginInternalItemDecoration(margin) { holder ->
            holder !is CardBottomCornersAdapterDelegate.Holder &&
                holder !is LabeledCardValueAdapterDelegate.Holder &&
                holder !is CardTitleItemAdapterDelegate.Holder &&
                holder !is DoubleLabeledCardValueAdapterDelegate.Holder &&
                holder !is CardDividerAdapterDelegate.Holder &&
                holder !is CardEmptyLineAdapterDelegate.Holder &&
                holder !is MorphologyCardAdapterDelegate.Holder &&
                holder !is CardTitleItemWitAccentAdapterDelegate.Holder
        }
    }

    private fun setState(state: MeasurementDetailsUiState) {
        measurementDetailsAdapter.setDataWithState(
            data = state.data,
            isNextPageLoading = false,
            isNextPageError = false,
            isPrevPageLoading = false,
            isPrevPageError = false,
            isLoading = state.loading,
            error = state.error
        )
        updateMeasurementButton.isEnabled = state.isMeasurementCreating.not() &&
            (state.isEditable || isPreviewMode)
        measurementCreatingProgress.isVisible = state.isMeasurementCreating
    }
}
