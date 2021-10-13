package ru.ktsstudio.app_verification.ui.object_survey.schedule

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.databinding.FragmentSurveyBinding
import ru.ktsstudio.app_verification.di.object_survey.schedule.ObjectScheduleSurveyComponent
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.TechnicalSurveyDraft
import ru.ktsstudio.app_verification.presentation.exit.ExitSurveyViewModel
import ru.ktsstudio.app_verification.presentation.object_survey.schedule.ObjectScheduleSurveyUiState
import ru.ktsstudio.app_verification.presentation.object_survey.schedule.ObjectScheduleSurveyViewModel
import ru.ktsstudio.app_verification.ui.object_survey.common.ExitSurveyDelegate
import ru.ktsstudio.app_verification.ui.object_survey.common.ExitSurveyDelegate.Companion.EXIT_DIALOG_TAG
import ru.ktsstudio.app_verification.ui.object_survey.common.SurveyAdapter
import ru.ktsstudio.common.ui.TimePickerDialogFragment
import ru.ktsstudio.common.ui.dialog.ConfirmDialogFragment
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.view.HideElevationScrollListener
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.utilities.extensions.toast
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 26.11.2020.
 */
class ObjectScheduleSurveyFragment : Fragment(R.layout.fragment_survey), ConfirmDialogFragment.OnClickListener {

    private val args: ObjectScheduleSurveyFragmentArgs by navArgs()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val binding: FragmentSurveyBinding by viewBinding(FragmentSurveyBinding::bind)
    private var surveyAdapter: SurveyAdapter<Unit> by autoCleared()

    private val viewModel: ObjectScheduleSurveyViewModel by viewModels { factory }

    private val exitSurveyViewModel: @JvmSuppressWildcards ExitSurveyViewModel<TechnicalSurveyDraft>
        by viewModels { factory }

    private lateinit var exitSurveyDelegate: ExitSurveyDelegate<TechnicalSurveyDraft>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ObjectScheduleSurveyComponent.create(args.objectId).inject(this)
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
            timePickerRequested = { initialTime, timeConsumeFunc ->
                TimePickerDialogFragment(initialTime) { time ->
                    viewModel.updateSurvey(timeConsumeFunc.invoke(time))
                }
                    .show(childFragmentManager, TIME_PICKER)
            }
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

    private fun initListeners() {
        binding.saveSurvey.setOnClickListener {
            viewModel.saveSurvey()
        }
    }

    private fun renderState(state: ObjectScheduleSurveyUiState) {
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

    companion object {
        private const val TIME_PICKER = "TIME_PICKER"
    }
}
