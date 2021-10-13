package ru.ktsstudio.app_verification.ui.object_survey.infrastructure

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.databinding.FragmentSurveyBinding
import ru.ktsstudio.app_verification.di.object_survey.infrastructure.InfrastructureSurveyComponent
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.TechnicalSurveyDraft
import ru.ktsstudio.app_verification.presentation.exit.ExitSurveyViewModel
import ru.ktsstudio.app_verification.presentation.media.MediaSurveyViewModel
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.InfrastructureSurveyUiState
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.InfrastructureSurveyViewModel
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.InfrastructureEquipmentType
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSurveyWithCheckAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.common.ExitSurveyDelegate
import ru.ktsstudio.app_verification.ui.object_survey.common.ExitSurveyDelegate.Companion.EXIT_DIALOG_TAG
import ru.ktsstudio.app_verification.ui.object_survey.common.MediaCaptureDelegate
import ru.ktsstudio.app_verification.ui.object_survey.common.SurveyAdapter
import ru.ktsstudio.common.ui.dialog.ConfirmDialogFragment
import ru.ktsstudio.common.ui.view.MarginInternalItemDecoration
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.view.HideElevationScrollListener
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.utilities.extensions.hideKeyboard
import ru.ktsstudio.utilities.extensions.toast
import javax.inject.Inject

class InfrastructureSurveyFragment : Fragment(R.layout.fragment_survey), ConfirmDialogFragment.OnClickListener {

    private val args: InfrastructureSurveyFragmentArgs by navArgs()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val binding: FragmentSurveyBinding by viewBinding(
        vbFactory = FragmentSurveyBinding::bind
    )

    private var surveyAdapter: SurveyAdapter<InfrastructureEquipmentType> by autoCleared()
    private lateinit var mediaCaptureDelegate: MediaCaptureDelegate<InfrastructureSurveyDraft>
    private lateinit var exitSurveyDelegate: ExitSurveyDelegate<TechnicalSurveyDraft>

    private val viewModel: InfrastructureSurveyViewModel by viewModels { factory }
    private val mediaSurveyViewModel: @JvmSuppressWildcards MediaSurveyViewModel<InfrastructureSurveyDraft>
        by viewModels { factory }
    private val exitSurveyViewModel: @JvmSuppressWildcards ExitSurveyViewModel<TechnicalSurveyDraft>
        by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        InfrastructureSurveyComponent.create(args.objectId).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initList()
        initListeners()
        bindViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDetach()
    }

    override fun onConfirmDialog(tag: String) {
        when (tag) {
            EXIT_DIALOG_TAG -> findNavController().popBackStack()
        }
    }

    private fun bindViewModel() {
        viewLifecycleOwner.observeNotNull(viewModel.state, ::renderState)
        viewLifecycleOwner.observeNotNull(viewModel.error, { toast(it) })
        mediaCaptureDelegate = MediaCaptureDelegate(
            fragment = this,
            mediaViewModel = mediaSurveyViewModel
        )
        exitSurveyDelegate = ExitSurveyDelegate(
            fragment = this,
            exitSurveyViewModel = exitSurveyViewModel
        )
    }

    private fun initToolbar() = with(binding.appBar.toolbar) {
        setTitle(R.string.object_inspection_toolbar_title)
        setNavigationIcon(R.drawable.ic_arrow_left)
        setNavigationOnClickListener { exitSurveyViewModel.exit() }
    }

    private fun initList() {
        surveyAdapter = SurveyAdapter(
            onRetry = viewModel::retry,
            onDataChanged = {
                (it as? ValueConsumer<*, *>)?.let { valueConsumer ->
                    if (valueConsumer.get() is Boolean) requireActivity().hideKeyboard()
                }
                viewModel.updateSurvey(it)
            },
            onMediaAdd = mediaSurveyViewModel::addMedia,
            onMediaDelete = mediaSurveyViewModel::deleteMedia,
            onAddEntity = viewModel::addEntity,
            onDeleteEntity = viewModel::deleteEntity
        )
        with(binding.list) {
            adapter = surveyAdapter
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

    private fun initListeners() {
        binding.saveSurvey.setOnClickListener {
            requireActivity().hideKeyboard()
            viewModel.saveSurvey()
        }
    }

    private fun renderState(state: InfrastructureSurveyUiState) {
        surveyAdapter.setDataWithState(
            data = state.data,
            isNextPageError = false,
            isNextPageLoading = false,
            isPrevPageError = false,
            isPrevPageLoading = false,
            isLoading = state.loading,
            error = state.error
        )
    }

    private fun getSpaceItemDecoration(): RecyclerView.ItemDecoration {
        val margin = requireContext().resources
            .getDimensionPixelSize(R.dimen.default_padding)
        return MarginInternalItemDecoration(margin) { holder ->
            holder is InnerLabeledSurveyWithCheckAdapterDelegate.Holder
        }
    }
}
