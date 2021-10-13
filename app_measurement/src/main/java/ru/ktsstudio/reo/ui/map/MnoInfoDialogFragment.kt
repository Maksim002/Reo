package ru.ktsstudio.reo.ui.map

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.dialog_mno_info.view.*
import ru.ktsstudio.common.ui.bottom_sheet.BottomSheetDialogFragmentWithRoundedCorners
import ru.ktsstudio.common.utils.navigateSafe
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.feature_map.ui.MapFragment
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.di.map.mno_info.MnoInfoComponent
import ru.ktsstudio.reo.presentation.map.MnoInfoUiState
import ru.ktsstudio.reo.presentation.map.MnoInfoViewModel
import ru.ktsstudio.reo.ui.map.adapter.MnoInfoAdapter
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 28.10.2020.
 */
class MnoInfoDialogFragment : BottomSheetDialogFragmentWithRoundedCorners<FrameLayout>(
    R.layout.dialog_mno_info
) {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: MnoInfoViewModel by viewModels { factory }

    private val args: MnoInfoDialogFragmentArgs by navArgs()

    private var mnoInfoAdapter: MnoInfoAdapter by autoCleared()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MnoInfoComponent.create(args.objectIds.toList()).inject(this)
    }

    override fun initView(contentView: View) {
        initViewModel()
        initList(contentView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDetach()
    }

    override fun onFullDestroy() {
        super.onFullDestroy()
        findNavController().currentBackStackEntry?.savedStateHandle?.set(
            MapFragment.INFO_DIALOG_ON_FULL_DESTROY_KEY,
            ""
        )
    }

    private fun initViewModel() {
        viewModel.state.observe(this, ::renderState)
    }

    private fun initList(contentView: View) {
        mnoInfoAdapter = MnoInfoAdapter(viewModel::retry) { mnoId ->
            findNavController().navigateSafe(
                MnoInfoDialogFragmentDirections.actionMnoInfoToMnoDetails(mnoId)
            )
        }

        contentView.mnoInfoList.adapter = mnoInfoAdapter
    }

    private fun renderState(state: MnoInfoUiState) {
        containerView?.slider?.isVisible = state.isLoading.not()
        mnoInfoAdapter.setDataWithState(
            data = state.info,
            isNextPageError = false,
            isNextPageLoading = false,
            isPrevPageError = false,
            isPrevPageLoading = false,
            isLoading = state.isLoading,
            error = state.error
        )
    }
}
