package ru.ktsstudio.app_verification.ui.object_survey.general

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.databinding.FragmentSurveyBinding
import ru.ktsstudio.app_verification.di.object_survey.general.GeneralSurveyComponent
import ru.ktsstudio.app_verification.presentation.object_survey.general.GeneralSurveyUiState
import ru.ktsstudio.app_verification.presentation.object_survey.general.GeneralSurveyViewModel
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledEditItemWithCheckAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledMultilineItemWithCheckAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledSelectorWithCheckAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LargeTitleItemAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.common.SurveyAdapter
import ru.ktsstudio.common.ui.dialog.ConfirmDialogFragment
import ru.ktsstudio.common.ui.view.MarginInternalItemDecoration
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.view.HideElevationScrollListener
import ru.ktsstudio.common.utils.view.autoCleared
import javax.inject.Inject

class GeneralSurveyFragment : Fragment(R.layout.fragment_survey), ConfirmDialogFragment.OnClickListener {

    private val args: GeneralSurveyFragmentArgs by navArgs()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val binding: FragmentSurveyBinding by viewBinding(FragmentSurveyBinding::bind)
    private var surveyAdapter: SurveyAdapter<Unit> by autoCleared()

    private val viewModel: GeneralSurveyViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GeneralSurveyComponent.create(args.objectId).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initList()
        initListeners()
        bindViewModel()
        addBackButtonCallback()
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
        viewLifecycleOwner.observeNotNull(viewModel.exit, { findNavController().popBackStack() })
        viewLifecycleOwner.observeNotNull(viewModel.exitDialog) { showExitDialog() }
    }

    private fun initToolbar() = with(binding.appBar.toolbar) {
        setTitle(R.string.object_inspection_toolbar_title)
        setNavigationIcon(R.drawable.ic_arrow_left)
        setNavigationOnClickListener { viewModel.exit() }
    }

    private fun initList() {
        surveyAdapter = SurveyAdapter(
            onRetry = viewModel::retry,
            onDataChanged = viewModel::updateSurvey
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

    private fun renderState(state: GeneralSurveyUiState) {
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
        return MarginInternalItemDecoration(
            spaceHeight = margin,
            needSpace = { holder ->
                holder !is LargeTitleItemAdapterDelegate.Holder &&
                    holder !is LabeledEditItemWithCheckAdapterDelegate.Holder &&
                    holder !is LabeledSelectorWithCheckAdapterDelegate.Holder<*> &&
                    holder !is LabeledMultilineItemWithCheckAdapterDelegate.Holder
            }
        )
    }

    private fun addBackButtonCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.exit()
        }
    }

    private fun showExitDialog() {
        ConfirmDialogFragment.getInstance(
            title = getString(R.string.exit_dialog_title),
            message = getString(R.string.exit_dialog_message),
            negative = getString(R.string.exit_dialog_reject),
            positive = getString(R.string.exit_dialog_leave),
            highlightPositive = true,
            tag = EXIT_DIALOG_TAG
        ).show(childFragmentManager, EXIT_DIALOG_TAG)
    }

    companion object {
        const val EXIT_DIALOG_TAG = "exit_dialog_tag"
    }
}
