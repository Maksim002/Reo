package ru.ktsstudio.app_verification.ui.object_survey.secondary_resources

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.databinding.FragmentSurveyBinding
import ru.ktsstudio.app_verification.di.object_survey.secondary_resource.SecondaryResourceSurveyComponent
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.TechnicalSurveyDraft
import ru.ktsstudio.app_verification.presentation.exit.ExitSurveyViewModel
import ru.ktsstudio.app_verification.presentation.object_survey.secondary_resources.SecondaryResourceEntity
import ru.ktsstudio.app_verification.presentation.object_survey.secondary_resources.SecondaryResourcesSurveyUiState
import ru.ktsstudio.app_verification.presentation.object_survey.secondary_resources.SecondaryResourcesSurveyViewModel
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledEditItemWithCheckAdapterDelegate
import ru.ktsstudio.app_verification.ui.object_survey.common.ExitSurveyDelegate
import ru.ktsstudio.app_verification.ui.object_survey.common.ExitSurveyDelegate.Companion.EXIT_DIALOG_TAG
import ru.ktsstudio.app_verification.ui.object_survey.common.SurveyAdapter
import ru.ktsstudio.common.ui.adapter.delegates.AddEntityAdapterDelegate
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
class SecondaryResourcesSurveyFragment : Fragment(R.layout.fragment_survey), ConfirmDialogFragment.OnClickListener {

    private val args: SecondaryResourcesSurveyFragmentArgs by navArgs()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val binding: FragmentSurveyBinding by viewBinding(FragmentSurveyBinding::bind)
    private var surveyAdapter: SurveyAdapter<SecondaryResourceEntity> by autoCleared()

    private val viewModel: SecondaryResourcesSurveyViewModel by viewModels { factory }
    private val exitSurveyViewModel: @JvmSuppressWildcards ExitSurveyViewModel<TechnicalSurveyDraft>
        by viewModels { factory }

    private lateinit var exitSurveyDelegate: ExitSurveyDelegate<TechnicalSurveyDraft>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SecondaryResourceSurveyComponent.create(args.objectId).inject(this)
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
        viewLifecycleOwner.observeNotNull(viewModel.error) { toast(it, Toast.LENGTH_LONG) }

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

    private fun getSpaceItemDecoration(): RecyclerView.ItemDecoration {
        val margin = requireContext().resources.getDimensionPixelSize(R.dimen.default_double_padding)
        return MarginInternalItemDecoration(margin, isTrailing = true) { holder ->
            holder is LabeledEditItemWithCheckAdapterDelegate.Holder ||
                holder is AddEntityAdapterDelegate.Holder<*>
        }
    }

    private fun initListeners() {
        binding.saveSurvey.setOnClickListener {
            viewModel.saveSurvey()
        }
    }

    private fun renderState(state: SecondaryResourcesSurveyUiState) {
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
}
