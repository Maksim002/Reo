package ru.ktsstudio.app_verification.ui.object_survey.tech

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.databinding.FragmentSurveyBinding
import ru.ktsstudio.app_verification.di.object_survey.tech.ObjectTechnicalSurveyComponent
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.TechnicalSurveyDraft
import ru.ktsstudio.app_verification.presentation.exit.ExitSurveyViewModel
import ru.ktsstudio.app_verification.presentation.media.MediaSurveyViewModel
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.TechnicalCardType
import ru.ktsstudio.app_verification.presentation.object_survey.tech.ObjectTechnicalSurveyUiState
import ru.ktsstudio.app_verification.presentation.object_survey.tech.ObjectTechnicalSurveyViewModel
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.app_verification.ui.object_survey.common.ExitSurveyDelegate
import ru.ktsstudio.app_verification.ui.object_survey.common.ExitSurveyDelegate.Companion.EXIT_DIALOG_TAG
import ru.ktsstudio.app_verification.ui.object_survey.common.MediaCaptureDelegate
import ru.ktsstudio.app_verification.ui.object_survey.common.SurveyAdapter
import ru.ktsstudio.common.ui.dialog.ConfirmDialogFragment
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.view.HideElevationScrollListener
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.utilities.extensions.hideKeyboard
import ru.ktsstudio.utilities.extensions.requireNotNull
import ru.ktsstudio.utilities.extensions.toast
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 02.12.2020.
 */
class ObjectTechnicalSurveyFragment : Fragment(R.layout.fragment_survey), ConfirmDialogFragment.OnClickListener {

    private val objectId: String
        get() = requireArguments().getString("objectId").requireNotNull()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val binding: FragmentSurveyBinding by viewBinding(FragmentSurveyBinding::bind)
    private var surveyAdapter: SurveyAdapter<TechnicalCardType> by autoCleared()

    private val viewModel: ObjectTechnicalSurveyViewModel by viewModels { factory }
    private val mediaSurveyViewModel: @JvmSuppressWildcards MediaSurveyViewModel<TechnicalSurveyDraft>
        by viewModels { factory }
    private val exitSurveyViewModel: @JvmSuppressWildcards ExitSurveyViewModel<TechnicalSurveyDraft>
        by viewModels { factory }

    private lateinit var mediaCaptureDelegate: MediaCaptureDelegate<TechnicalSurveyDraft>
    private lateinit var exitSurveyDelegate: ExitSurveyDelegate<TechnicalSurveyDraft>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ObjectTechnicalSurveyComponent.create(objectId).inject(this)
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
            onDataChanged = ::onDataChanged,
            onMediaAdd = mediaSurveyViewModel::addMedia,
            onMediaDelete = mediaSurveyViewModel::deleteMedia,
            onAddEntity = viewModel::addEntity,
            onDeleteEntity = viewModel::deleteEntity
        )
        with(binding.list) {
            adapter = surveyAdapter
            setHasFixedSize(true)
            addOnScrollListener(
                HideElevationScrollListener(
                    binding.bottomBlock,
                    resources.getDimensionPixelSize(R.dimen.default_elevation)
                )
            )
        }
    }

    private fun onDataChanged(updater: Updater<*>) {
        (updater as? ValueConsumer<*, *>)?.let { valueConsumer ->
            if (valueConsumer.get() is Boolean) requireActivity().hideKeyboard()
        }
        viewModel.updateSurvey(updater)
    }

    private fun initListeners() {
        binding.saveSurvey.setOnClickListener {
            viewModel.saveSurvey()
        }
    }

    private fun renderState(state: ObjectTechnicalSurveyUiState) {
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
}
