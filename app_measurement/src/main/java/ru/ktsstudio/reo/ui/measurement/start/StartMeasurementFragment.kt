package ru.ktsstudio.reo.ui.measurement.start

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_start_measurement.*
import ru.ktsstudio.common.ui.fragment.BaseFragment
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.setValueWithoutEventTrigger
import ru.ktsstudio.common.utils.view.AfterTextChangedWatcher
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.di.app.MeasurementAppComponent
import ru.ktsstudio.reo.presentation.measurement.start.StartMeasurementUiState
import ru.ktsstudio.reo.presentation.measurement.start.StartMeasurementViewModel
import ru.ktsstudio.utilities.extensions.hideKeyboard
import ru.ktsstudio.utilities.extensions.hideKeyboardFrom
import ru.ktsstudio.utilities.extensions.toast
import javax.inject.Inject

/**
 * Created by Igor Park on 14/10/2020.
 */
class StartMeasurementFragment : BaseFragment(R.layout.fragment_start_measurement) {

    private val args: StartMeasurementFragmentArgs by navArgs()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val locationPermissionCallback = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionState ->
        viewModel.skipMeasurement(
            mnoId = args.mnoId,
            impossibilityReason = reasonInput.text?.toString().orEmpty(),
            withLocation = permissionState.all { it.value }
        )
    }

    private val textListener by lazy(LazyThreadSafetyMode.NONE) {
        AfterTextChangedWatcher(viewModel::addComment)
    }

    private val viewModel: StartMeasurementViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViews()
        bindViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDetach()
    }

    override fun onFullDestroy() {
        super.onFullDestroy()
        requireActivity().hideKeyboard()
    }

    private fun initToolbar() = with(requireView().findViewById<Toolbar>(R.id.toolbar)) {
        setTitle(R.string.measurement_hold_measure)
        setNavigationIcon(R.drawable.ic_arrow_left)
        setNavigationOnClickListener {
            findNavController().popBackStack()
            requireActivity().hideKeyboard()
        }
    }

    private fun initViews() {
        yesButton.setOnClickListener { viewModel.setMnoActiveState(true) }
        noButton.setOnClickListener { viewModel.setMnoActiveState(false) }
        reasonInput.addTextChangedListener(textListener)

        resolveMeasurementButton.setOnClickListener {
            viewModel.resolveMeasurement(mnoId = args.mnoId)
        }
        reasonInput.addTextChangedListener(textListener)
    }

    private fun bindViewModel() {
        with(viewModel) {
            viewLifecycleOwner.observeNotNull(errorMessage, { toast(it) })
            viewLifecycleOwner.observeNotNull(state, ::renderState)
            viewLifecycleOwner.observeNotNull(
                skipMeasurement,
                {
                    locationPermissionCallback.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            )
        }
    }

    private fun inject() {
        ComponentRegistry.get<MeasurementAppComponent>()
            .startMeasurementComponent()
            .inject(this)
    }

    private fun renderState(state: StartMeasurementUiState) {
        val isCommentGroupVisible = state.isMnoActive.not() && state.isOptionSelected
        commentGroup.isVisible = isCommentGroupVisible

        reasonInput.setValueWithoutEventTrigger(state.comment, textListener)
        if (isCommentGroupVisible.not()) {
            reasonInput.removeTextChangedListener(textListener)
        }
        resolveMeasurementButton.isVisible = state.isOptionSelected
        resolveMeasurementButton.isEnabled = state.isReady && state.isLoading.not()
        if (state.isMnoActive) {
            reasonInput.text = null
            requireActivity().hideKeyboardFrom(reasonInput)
        }
        resolveMeasurementButton.text = state.measurementActionText
        progress.isVisible = state.isLoading
    }
}
