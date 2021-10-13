package ru.ktsstudio.app_verification.ui.object_survey.equipment

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
import ru.ktsstudio.app_verification.di.object_survey.equipment.EquipmentSurveyComponent
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.EquipmentSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.TechnicalSurveyDraft
import ru.ktsstudio.app_verification.presentation.exit.ExitSurveyViewModel
import ru.ktsstudio.app_verification.presentation.media.MediaSurveyViewModel
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.EquipmentEntity
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.EquipmentSurveyUiState
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.EquipmentSurveyViewModel
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.SubtitleItemWithCheckAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.common.ExitSurveyDelegate
import ru.ktsstudio.app_verification.ui.object_survey.common.ExitSurveyDelegate.Companion.EXIT_DIALOG_TAG
import ru.ktsstudio.app_verification.ui.object_survey.common.MediaCaptureDelegate
import ru.ktsstudio.app_verification.ui.object_survey.common.SurveyAdapter
import ru.ktsstudio.common.ui.dialog.ConfirmDialogFragment
import ru.ktsstudio.common.ui.view.MarginInternalItemDecoration
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.view.HideElevationScrollListener
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.utilities.extensions.toast
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 10.12.2020.
 */
class EquipmentSurveyFragment : Fragment(R.layout.fragment_survey), ConfirmDialogFragment.OnClickListener {

    private val args: EquipmentSurveyFragmentArgs by navArgs()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val binding: FragmentSurveyBinding by viewBinding(FragmentSurveyBinding::bind)
    private var surveyAdapter: SurveyAdapter<EquipmentEntity> by autoCleared()

    private val viewModel: EquipmentSurveyViewModel by viewModels { factory }
    private val mediaSurveyViewModel: @JvmSuppressWildcards MediaSurveyViewModel<EquipmentSurveyDraft>
        by viewModels { factory }
    private val exitSurveyViewModel: @JvmSuppressWildcards ExitSurveyViewModel<TechnicalSurveyDraft>
        by viewModels { factory }

    private lateinit var mediaCaptureDelegate: MediaCaptureDelegate<EquipmentSurveyDraft>
    private lateinit var exitSurveyDelegate: ExitSurveyDelegate<TechnicalSurveyDraft>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EquipmentSurveyComponent.create(args.objectId).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initListeners()
        initList()
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
            onDataChanged = viewModel::updateSurvey,
            onAddEntity = viewModel::addEntity,
            onDeleteEntity = viewModel::deleteEntity,
            onMediaAdd = mediaSurveyViewModel::addMedia,
            onMediaDelete = mediaSurveyViewModel::deleteMedia
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
            viewModel.saveSurvey()
        }
    }

    private fun renderState(state: EquipmentSurveyUiState) {
        surveyAdapter.setDataWithState(
            data = state.data,
            isNextPageLoading = false,
            isNextPageError = false,
            isPrevPageLoading = false,
            isPrevPageError = false,
            isLoading = state.loading,
            error = state.error
        )
    }

    private fun getSpaceItemDecoration(): RecyclerView.ItemDecoration {
        val margin = requireContext().resources
            .getDimensionPixelSize(R.dimen.default_double_padding)
        return MarginInternalItemDecoration(margin) { holder ->
            holder is SubtitleItemWithCheckAdapterDelegate.Holder
        }
    }
}
