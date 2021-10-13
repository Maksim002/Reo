package ru.ktsstudio.app_verification.ui.object_survey.common

import android.content.res.Resources
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.presentation.exit.ExitSurveyViewModel
import ru.ktsstudio.common.ui.dialog.ConfirmDialogFragment
import ru.ktsstudio.common.utils.observeNotNull

/**
 * @author Maxim Ovchinnikov on 10.02.2021.
 */
internal class ExitSurveyDelegate<DraftType>(
    private val fragment: Fragment,
    private val exitSurveyViewModel: ExitSurveyViewModel<DraftType>
) {
    private val resources: Resources
        get() = fragment.resources

    init {
        observeViewModel()
        addBackButtonCallback()
    }

    private fun observeViewModel() = with(exitSurveyViewModel) {
        fragment.viewLifecycleOwner.observeNotNull(exit) { fragment.findNavController().popBackStack() }
        fragment.viewLifecycleOwner.observeNotNull(exitDialog) { showExitDialog() }
    }

    private fun addBackButtonCallback() {
        fragment.requireActivity().onBackPressedDispatcher.addCallback(fragment) {
            exitSurveyViewModel.exit()
        }
    }

    private fun showExitDialog() {
        ConfirmDialogFragment.getInstance(
            title = resources.getString(R.string.exit_dialog_title),
            message = resources.getString(R.string.exit_dialog_message),
            negative = resources.getString(R.string.exit_dialog_reject),
            positive = resources.getString(R.string.exit_dialog_leave),
            highlightPositive = true,
            tag = EXIT_DIALOG_TAG
        ).show(fragment.childFragmentManager, EXIT_DIALOG_TAG)
    }

    companion object {
        const val EXIT_DIALOG_TAG = "exit_dialog_tag"
    }
}
