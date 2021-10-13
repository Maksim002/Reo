package ru.ktsstudio.app_verification.ui.object_inspection

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.databinding.FragmentObjectInspectionBinding
import ru.ktsstudio.app_verification.di.object_inspection.ObjectInspectionComponent
import ru.ktsstudio.app_verification.presentation.object_inspection.ObjectInspectionUiState
import ru.ktsstudio.app_verification.presentation.object_inspection.ObjectInspectionViewModel
import ru.ktsstudio.common.ui.view.MarginInternalItemDecoration
import ru.ktsstudio.common.utils.exhaustive
import ru.ktsstudio.common.utils.navigateSafe
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.view.HideElevationScrollListener
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.core_data_verfication_api.data.model.SurveySubtype
import ru.ktsstudio.utilities.extensions.hideKeyboard
import ru.ktsstudio.utilities.extensions.toast
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 20.11.2020.
 */
class ObjectInspectionFragment : Fragment(R.layout.fragment_object_inspection) {

    private val args: ObjectInspectionFragmentArgs by navArgs()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val binding: FragmentObjectInspectionBinding by viewBinding(
        vbFactory = FragmentObjectInspectionBinding::bind
    )

    private var progressCardAdapter: ProgressCardAdapter by autoCleared()
    private val viewModel: ObjectInspectionViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ObjectInspectionComponent.create(args.objectId).inject(this)
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

    private fun bindViewModel() {
        viewLifecycleOwner.observeNotNull(viewModel.state, ::setState)
        viewLifecycleOwner.observeNotNull(viewModel.error, { toast(it) })
        viewLifecycleOwner.observeNotNull(viewModel.navigate, { findNavController().popBackStack() })
    }

    private fun initToolbar() = with(binding.appBar.toolbar) {
        setTitle(R.string.object_inspection_toolbar_title)
        setNavigationIcon(R.drawable.ic_arrow_left)
        setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun initViews() {
        progressCardAdapter = ProgressCardAdapter(
            onRetry = viewModel::retry,
            onCardClick = ::openSurvey
        )
        binding.sendInspectionButton.setOnClickListener {
            findNavController().popBackStack()
        }
        with(binding.progressList) {
            adapter = progressCardAdapter
            setHasFixedSize(true)
            addItemDecoration(getSpaceItemDecoration())
            addOnScrollListener(
                HideElevationScrollListener(
                    binding.bottomBlock,
                    resources.getDimensionPixelSize(R.dimen.default_elevation)
                )
            )
        }
    }

    private fun setState(state: ObjectInspectionUiState) = with(state) {
        progressCardAdapter.setDataWithState(
            data = progressList,
            isNextPageError = false,
            isNextPageLoading = false,
            isPrevPageError = false,
            isPrevPageLoading = false,
            isLoading = isLoading,
            error = error
        )
        binding.sendInspectionButton.isEnabled = isReady
        binding.surveySendingProgress.isVisible = isSending
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }

    private fun openSurvey(type: SurveySubtype) {
        when (type) {
            SurveySubtype.SCHEDULE -> {
                findNavController().navigateSafe(
                    ObjectInspectionFragmentDirections.actionInspectionToObjectScheduleSurvey(args.objectId)
                )
            }
            SurveySubtype.EQUIPMENT -> {
                findNavController().navigateSafe(
                    ObjectInspectionFragmentDirections.actionObjectInspectionFragmentToEquipmentSurveyFragment(
                        args.objectId
                    )
                )
            }
            SurveySubtype.INFRASTRUCTURAL -> {
                findNavController().navigateSafe(
                    ObjectInspectionFragmentDirections
                        .actionObjectInspectionFragmentToInfrastructureSurveyFragment(args.objectId)
                )
            }
            SurveySubtype.TECHNICAL -> findNavController().navigateSafe(
                ObjectInspectionFragmentDirections.actionInspectionToObjectTechnicalSurvey(args.objectId)
            )
            SurveySubtype.GENERAL -> {
                findNavController().navigateSafe(
                    ObjectInspectionFragmentDirections.actionInspectionToGeneralSurvey(args.objectId)
                )
            }
            SurveySubtype.SECONDARY_RESOURCE -> findNavController().navigateSafe(
                ObjectInspectionFragmentDirections
                    .actionObjectInspectionFragmentToSecondaryResourcesSurveyFragment(args.objectId)
            )
            SurveySubtype.PRODUCTION -> {
                findNavController().navigateSafe(
                    ObjectInspectionFragmentDirections
                        .actionInspectionToProductSurvey(args.objectId)
                )
            }
        }.exhaustive
    }

    private fun getSpaceItemDecoration(): RecyclerView.ItemDecoration {
        val margin = requireContext().resources
            .getDimensionPixelSize(R.dimen.default_padding)
        return MarginInternalItemDecoration(
            spaceHeight = margin,
            needSpace = { true }
        )
    }
}
